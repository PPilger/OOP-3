import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * NOTE: Speichert Ort, Zeitraum, Dauer ab. Bietet Methoden fuer die kaufmaennische
 * Berechnungslehre.
 * 
 * @author Koegler Alexander
 */
public class Termin implements Serializable {
	private static final long serialVersionUID = 1L;

	private Typ typus;
	private Ort ort;
	private Zeitraum zeitraum;
	private Posten posten;

	// NOTE: aenderungen sind nicht zugelassen, duerfen nur mittels methoden der klasse veraendert werden
	// Liste darf keine NULL Werte enthalten, oder doppelte eintraege
	private List<Mitglied> teilnehmer;

	//NOTE: speichert vorhergehenden zustand des Objekts (UNDO), ist NULL wenn instanziert, oder nichts mehr rueckgaengig gemacht werden kann
	private Termin orig;

	private Termin() {
	}

	/**
	 * NOTE: Legt neuen Termin an
	 * 
	 * Vorbedingung: Parameter duerfen nicht NULL sein, private variable orig muss NULL bleiben. Liste darf keine NULL Werte enthalten, oder doppelte eintraege. Double Werte muessen das positives Vorzeichen haben.
	 * 
	 * @param typus
	 * @param ort
	 * @param von
	 * @param bis
	 * @param ausgaben muss >= 0 sein
	 * @param einnahmen muss >= 0 sein
	 * @param teilnehmer
	 */
	public Termin(Typ typus, Ort ort, Date von, Date bis, double ausgaben,
			double einnahmen, List<Mitglied> teilnehmer) {
		this.typus = typus;
		this.ort = ort;
		this.zeitraum = new Zeitraum(von, bis);
		this.posten = new Posten(einnahmen, ausgaben, typus.toString(), bis);
		this.teilnehmer = teilnehmer;
		this.orig = null;
	}
	
	public Zeitraum getZeitraum() {
		return zeitraum;
	}
	
	public Posten getPosten() {
		return posten;
	}

	/**
	 * Nachbedingung: liefert Zahl >= 0 zurueck
	 * 
	 * @return
	 */
	public double getAusgaben() {
		return posten.getAusgaben();
	}

	/**
	 * Nachbedingung: liefert Zahl >= 0 zurueck
	 * 
	 * @return
	 */
	public double getEinnahmen() {
		return posten.getEinnahmen();
	}

	/**
	 * Vorbedingung: Liste darf keine NULL Werte enthalten, oder doppelte eintraege.
	 * 
	 * @return Teilnehmerliste. Diese darf nicht geaendert werden!
	 */
	public List<Mitglied> getTeilnehmer() {
		return teilnehmer;
	}

	/**
	 * NOTE: Legt eine Kopie des Termins auf den Undo-Stack.
	 */
	private void prepareUpdate() {
		Termin other = new Termin();
		other.typus = typus;

		// NOTE: flache kopie (kann nicht geaendert werden, da privat)
		other.zeitraum = zeitraum;

		// NOTE: flache kopie (eine aenderung in ort aendert nichts an der bedeutung)
		other.ort = ort;

		// NOTE: flache kopie (unveraenderbar)
		other.posten = posten;

		// NOTE: flache kopie (aenderungen sind nicht zugelassen)
		other.teilnehmer = teilnehmer;

		// NOTE: haenge other hinter this in die historie ein
		other.orig = orig;
		this.orig = other;
	}

	public boolean undo() {
		if (orig == null) {
			return false;
		}

		meldeUpdate("zurueckgesetzt auf vorige Version");

		this.typus = orig.typus;
		this.ort = orig.ort;
		this.zeitraum = orig.zeitraum;
		this.posten = orig.posten;
		this.teilnehmer = orig.teilnehmer;
		this.orig = orig.orig;

		return true;
	}

	/**
	 * NOTE: Benachrichtigt alle Teilnehmer ueber die gemachte Aenderung.
	 * 
	 * Nachbedinung: stellt nicht sicher, dass Teilnehmer benachrichtigt werden
	 * 
	 * @param aenderung
	 */
	private void meldeUpdate(String aenderung) {
		for (Mitglied t : teilnehmer) {
			assert(t != null);
			//NOTE: t darf nicht doppelt in teilnehmer vorhanden sein
			t.sende(orig + " wurde geaendert: " + aenderung);
		}
	}

	/**
	 * Vorbedingung: Parameter zum ueberspeichern des Ortes darf nicht NULL sein
	 * Nachbedinung: stellt nicht sicher, dass Teilnehmer benachrichtigt werden
	 * 
	 * @author Christian Kletzander
	 * @param ort
	 */
	public void setOrt(Ort ort) {
		this.prepareUpdate();
		this.ort = ort;
		this.meldeUpdate(orig.ort + " -> " + ort);
	}

	/**
	 * Vorbedingung: Parameter zum ueberspeichern des Zeitraums duerfen nicht NULL sein
	 * Nachbedinung: stellt nicht sicher, dass Teilnehmer benachrichtigt werden
	 * 
	 * @author Christian Kletzander
	 * @param zeitraum
	 */
	public void setZeitraum(Date von, Date bis) {
		this.prepareUpdate();
		this.zeitraum = new Zeitraum(von, bis);
		this.meldeUpdate(orig.zeitraum + " -> " + zeitraum);
	}

	/**
	 * Vorbedingung: Parameter muessen >= 0 sein
	 * Nachbedinung: stellt nicht sicher, dass Teilnehmer benachrichtigt werden
	 * 
	 * @param kosten
	 */
	public void setAusgaben(double kosten) {
		this.prepareUpdate();
		this.posten = new Posten(posten.getEinnahmen(), kosten,
				typus.toString(), zeitraum.getLast());
		this.meldeUpdate("Kosten: " + orig.getAusgaben() + " -> "
				+ getAusgaben());
	}

	/**
	 * Vorbedingung: Parameter muessen >= 0 sein
	 * Nachbedinung: stellt nicht sicher, dass Teilnehmer benachrichtigt werden
	 * 
	 * @param umsatz
	 */
	public void setEinnahmen(double umsatz) {
		this.prepareUpdate();
		this.posten = new Posten(umsatz, posten.getAusgaben(),
				typus.toString(), zeitraum.getLast());
		this.meldeUpdate("Umsatz: " + orig.getEinnahmen() + " -> "
				+ getEinnahmen());
	}

	@Override
	public String toString() {
		return typus + ": " + ort + " "
				+ zeitraum.toString(new SimpleDateFormat("dd.MM.yyyy hh:mm"));
	}

	public String toDetailString() {
		return String.format("%s, Kosten: %,.2f, Umsatz: %,.2f", toString(),
				posten.getAusgaben(), posten.getEinnahmen());
	}

	public static enum Typ {
		Probe, Auftritt;
	}

	/**
	 * NOTE: Selektiert jene Termine an denen ein gegebenes Mitglied auch beteiligt
	 * ist
	 * 
	 * Vorbedingung: Parameter duerfen nicht NULL sein
	 * 
	 * @author Koegler Alexander
	 */
	public static class TeilnehmerSelektor implements Selector<Termin> {
		private Mitglied m;

		//Parameter sollte nicht NULL sein
		public TeilnehmerSelektor(Mitglied m) {
			this.m = m;
		}

		@Override
		public boolean select(Termin item) {
			return item.teilnehmer.contains(m);
		}
	}

	/**
	 * NOTE: Selektiert Termine dessen Zeitraum den angegebenen ueberschneidet
	 * 
	 * Vorbedingung: Parameter duerfen nicht NULL sein
	 * 
	 * @author Koegler Alexander
	 */
	public static class ZeitraumSelektor implements Selector<Termin> {

		private Zeitraum zeitraum;

		//Parameter darf nicht NULL sein
		public ZeitraumSelektor(Zeitraum zeitraum) {
			this.zeitraum = zeitraum;
		}

		@Override
		//Parameter darf nicht NULL sein
		public boolean select(Termin item) {
			return this.zeitraum.enthaelt(item.zeitraum);
		}

	}

	/**
	 * NOTE: Selektiert Termine aus, dessen Typ mit dem angegebenen uebereinstimmt
	 * 
	 * Vorbedingung: Parameter duerfen nicht NULL sein
	 * 
	 * @author Koegler Alexander
	 */
	public static class TypSelektor implements Selector<Termin> {
		private Typ typus;

		public TypSelektor(Typ typus) {
			this.typus = typus;
		}

		@Override
		//Parameter darf nicht NULL sein
		public boolean select(Termin item) {
			return this.typus == item.typus;
		}

	}
}

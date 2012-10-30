import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**GOOD: Enumeration, diese wird statt Interface oder Vererbung fuer Typ verwendet, da auf diese Art Code "wiederverwendet" wird.
 * Man erspart sich mehrere Klassen, deren unterschied nur der Typ ist.
 * Aufgrund der Selektoren kann ein Typ bei der weiteren verarbeitung Problemlos ausgewaehlt werden,
 * man erspart sich somit Typueberpruefungen. Ein Enumerator ermoeglicht auch die spaetere Erwaeiterbarkeit der Klasse in hinblick auf andere Typen.
 * 
 * BAD: schwacher KLassenzusammenhalt. EIn 
 * NOTE: Speichert Ort, Zeitraum, Dauer ab. Bietet Methoden fuer die
 * kaufmaennische Berechnungslehre.
 * 
 * Invariante: typus, ort, zeitraum, posten und teilnehmer sind ungleich null.
 * teilnehmer enthaelt keine Elemente gleich null. teilnehmer enthaelt keine
 * doppelten Eintraege.
 * 
 * @author Koegler Alexander
 */
public class Termin implements Serializable {
	private static final long serialVersionUID = 1L;

	private Typ typus;
	private Ort ort;
	private Zeitraum zeitraum;
	private Posten posten;

	// NOTE: aenderungen sind nicht zugelassen, duerfen nur mittels methoden der
	// klasse veraendert werden. Liste darf keine NULL Werte enthalten, oder
	// doppelte eintraege
	private List<Mitglied> teilnehmer;

	// NOTE: speichert vorhergehenden zustand des Objekts (UNDO), ist NULL wenn
	// instanziert, oder nichts mehr rueckgaengig gemacht werden kann
	private Termin orig;

	private Termin() {
	}

	/**
	 * NOTE: Legt neuen Termin an
	 * 
	 * Vorbedingung: typus, ort, von, bis und teilnehmer sind ungleich null.
	 * 
	 * Vorbedingung: von ist ein Zeitpunkt vor bis
	 * 
	 * Vorbedingung: einnahmen und ausgaben sind >= 0
	 * 
	 * Vorbedingung: teilnehmer enthaelt keine Elemente gleich null. teilnehmer
	 * enthaelt keine doppelten Eintraege.
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

	/**
	 * Nachbedingung: der Rueckgabewert ist ungleich null
	 */
	public Zeitraum getZeitraum() {
		return zeitraum;
	}

	/**
	 * Nachbedingung: der Rueckgabewert ist ungleich null
	 */
	public Posten getPosten() {
		return posten;
	}

	/**
	 * Nachbedingung: der Rueckgabewert ist >= 0
	 */
	public double getAusgaben() {
		return posten.getAusgaben();
	}

	/**
	 * Nachbedingung: der Rueckgabewert ist >= 0
	 */
	public double getEinnahmen() {
		return posten.getEinnahmen();
	}

	/**
	 * Nachbedingung: der Rueckgabewert enthaelt keine Elemente gleich null und
	 * enthaelt keine doppelten Eintraege.
	 * 
	 * @return Teilnehmerliste. Diese darf nicht geaendert werden!
	 */
	public List<Mitglied> getTeilnehmer() {
		return teilnehmer;
	}

	/**
	 * NOTE: Legt eine Kopie des Termins auf den Undo-Stack.
	 * 
	 * Nachbedingung: eine Kopie des this-Objektes steht in der verketteten
	 * (undo)-Liste nach dem this-Objekt.
	 */
	private void prepareUpdate() {
		Termin other = new Termin();
		other.typus = typus;

		// NOTE: flache kopie (kann nicht geaendert werden, da privat)
		other.zeitraum = zeitraum;

		// NOTE: flache kopie (eine aenderung in ort aendert nichts an der
		// bedeutung)
		other.ort = ort;

		// NOTE: flache kopie (unveraenderbar)
		other.posten = posten;

		// NOTE: flache kopie (aenderungen sind nicht zugelassen)
		other.teilnehmer = teilnehmer;

		// NOTE: haenge other hinter this in die historie ein
		other.orig = orig;
		this.orig = other;
	}

	/**
	 * Nachbedingung: Wenn der Rueckgabewert true ist, ist der letzte
	 * Objektzustand, der in der verketten undo-Liste gespeichert wurde, wieder
	 * hergestellt. Anderenfalls ist der Termin unveraendert.
	 * 
	 * Nachbedingung: Wenn der Rueckgabewert true ist, befindet sich in den
	 * Nachrichten-Queues der Teilnehmer eine Nachricht ueber die Aenderung des
	 * Termins.
	 */
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
	 * Nachbedingung: In den Nachrichten-Queues der Teilnehmer befindet sich
	 * eine Nachricht ueber die Aenderung des Termins.
	 * 
	 * @param aenderung
	 */
	private void meldeUpdate(String aenderung) {
		for (Mitglied t : teilnehmer) {
			// NOTE: t darf nicht doppelt in teilnehmer vorhanden sein
			t.sende(orig + " wurde geaendert: " + aenderung);
		}
	}

	/**
	 * Vorbedingung: ort ist ungleich null
	 * 
	 * Nachbedingung: der Ort des Termins ist nun ort.
	 * 
	 * Nachbedingung: der alte Zustand des Termins ist in der verketteten
	 * undo-Liste gespeichert.
	 * 
	 * Nachbedingung: alle Teilnehmer des Termins haben eine Nachricht ueber die
	 * Aenderung erhalten.
	 * 
	 * @author Christian Kletzander
	 */
	public void setOrt(Ort ort) {
		this.prepareUpdate();
		this.ort = ort;
		this.meldeUpdate(orig.ort + " -> " + ort);
	}

	/**
	 * Vorbedingung: von und bis sind ungleich null
	 * 
	 * Vorbedingung: von ist ein Zeitpunkt vor bis
	 * 
	 * Nachbedingung: der Zeitraum des Termins ist nun das Intervall [von, bis].
	 * 
	 * Nachbedingung: der alte Zustand des Termins ist in der verketteten
	 * undo-Liste gespeichert.
	 * 
	 * Nachbedingung: alle Teilnehmer des Termins haben eine Nachricht ueber die
	 * Aenderung erhalten.
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
	 * Vorbedingung: kosten ist >= 0
	 * 
	 * Nachbedingung: die Ausgaben des Termins sind nun kosten
	 * 
	 * Nachbedingung: der alte Zustand des Termins ist in der verketteten
	 * undo-Liste gespeichert.
	 * 
	 * Nachbedingung: alle Teilnehmer des Termins haben eine Nachricht ueber die
	 * Aenderung erhalten.
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
	 * Vorbedingung: umsatz ist >= 0
	 * 
	 * Nachbedingung: die Einnahmen des Termins sind nun umsatz
	 * 
	 * Nachbedingung: der alte Zustand des Termins ist in der verketteten
	 * undo-Liste gespeichert.
	 * 
	 * Nachbedingung: alle Teilnehmer des Termins haben eine Nachricht ueber die
	 * Aenderung erhalten.
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
	/**
	 * Nachbedingung: der Rueckgabewert ist ungleich null
	 */
	public String toString() {
		return typus + ": " + ort + " "
				+ zeitraum.toString(new SimpleDateFormat("dd.MM.yyyy hh:mm"));
	}

	/**
	 * Nachbedingung: der Rueckgabewert ist ungleich null
	 */
	public String toDetailString() {
		return String.format("%s, Kosten: %,.2f, Umsatz: %,.2f", toString(),
				posten.getAusgaben(), posten.getEinnahmen());
	}

	public static enum Typ {
		Probe, Auftritt;
	}

	/**
	 * NOTE: Selektiert jene Termine an denen ein gegebenes Mitglied auch
	 * beteiligt ist
	 * 
	 * @author Koegler Alexander
	 */
	public static class TeilnehmerSelektor implements Selector<Termin> {
		private Mitglied m;

		public TeilnehmerSelektor(Mitglied m) {
			this.m = m;
		}

		@Override
		/**
		 * Vorbedingung: item ist ungleich null
		 */
		public boolean select(Termin item) {
			return item.teilnehmer.contains(m);
		}
	}

	/**
	 * NOTE: Selektiert Termine dessen Zeitraum den angegebenen ueberschneidet
	 * 
	 * Invariante: zeitraum ist ungleich null
	 * 
	 * @author Koegler Alexander
	 */
	public static class ZeitraumSelektor implements Selector<Termin> {

		private Zeitraum zeitraum;

		/**
		 * Vorbedingung: zeitraum ist ungleich null
		 */
		public ZeitraumSelektor(Zeitraum zeitraum) {
			this.zeitraum = zeitraum;
		}

		@Override
		/**
		 * Vorbedingung: item ist ungleich null
		 */
		public boolean select(Termin item) {
			return this.zeitraum.enthaelt(item.zeitraum);
		}

	}

	/**
	 * NOTE: Selektiert Termine aus, dessen Typ mit dem angegebenen
	 * uebereinstimmt
	 * 
	 * Invariante: typus ist ungleich null
	 * 
	 * @author Koegler Alexander
	 */
	public static class TypSelektor implements Selector<Termin> {
		private Typ typus;

		/**
		 * Vorbedingung: typus ist ungleich null
		 */
		public TypSelektor(Typ typus) {
			this.typus = typus;
		}

		@Override
		/**
		 * Vorbedingung: item ist ungleich null
		 */
		public boolean select(Termin item) {
			return this.typus == item.typus;
		}

	}
}

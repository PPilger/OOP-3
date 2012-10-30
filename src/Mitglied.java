import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

/**
 * NOTE: Speichert Personenspezifische Daten, und kann Nachrichten, sowie
 * Terminvorschlaege empfangen
 * 
 * Invariante: keine Objektvariable ist null, nachrichten und terminvorschlaege
 * enthalten keine Elemente gleich null
 * 
 * @author Christian Kletzander
 */
public class Mitglied implements Serializable {
	private static final long serialVersionUID = 1L;

	// NOTE: Variablendefinition
	private String name;
	private String telNr;
	private String instrument;
	private Zeitraum zeitraum;
	private Queue<String> nachrichten;
	private Queue<Terminvorschlag> terminvorschlaege;
	private boolean ersatzmitglied;

	/**
	 * Vorbedingung: Parameter duerfen nicht null sein.
	 * 
	 * Nachbedingung: Queues sind leer
	 * 
	 * @param name
	 * @param telNr
	 * @param instrument
	 * @param zeitraum
	 * @param ersatzmitglied
	 */
	public Mitglied(String name, String telNr, String instrument,
			Zeitraum zeitraum, boolean ersatzmitglied) {
		this.name = name;
		this.telNr = telNr;
		this.instrument = instrument;
		this.zeitraum = zeitraum;
		this.nachrichten = new LinkedList<String>();
		this.terminvorschlaege = new LinkedList<Terminvorschlag>();
		this.ersatzmitglied = ersatzmitglied;
	}

	public Zeitraum getZeitraum() {
		return this.zeitraum;
	}

	/**
	 * Vorbedingung: nachricht ist ungleich null
	 * 
	 * Nachbedingung: die Queue nachrichten enthaelt nachricht
	 * 
	 * @param nachricht
	 */
	public void sende(String nachricht) {
		this.nachrichten.offer(nachricht);
	}

	/**
	 * Vorbedingung: terminvorschlag ist ungleich null
	 * 
	 * Nachbedingung: die Queue terminvorschlaege enthaelt terminvorschlag
	 * 
	 * @param terminvorschlag
	 */
	public void sende(Terminvorschlag terminvorschlag) {
		this.terminvorschlaege.offer(terminvorschlag);
	}

	/**
	 * Nachbedingung: terminvorschlag ist nichtmehr in der
	 * Terminvorschlag-Warteschlange
	 */
	public void revidiere(Terminvorschlag terminvorschlag) {
		this.terminvorschlaege.remove(terminvorschlag);
	}

	/**
	 * Nachbedingung: der Rueckgabewert ist ungleich null und er enthaelt keine
	 * Elemente gleich null
	 */
	public Queue<String> getNachrichten() {
		return nachrichten;
	}

	/**
	 * Nachbedingung: der Rueckgabewert ist ungleich null und er enthaelt keine
	 * Elemente gleich null
	 */
	public Queue<Terminvorschlag> getTerminvorschlaege() {
		return terminvorschlaege;
	}

	public void setErsatzmitglied(boolean ersatzmitglied) {
		this.ersatzmitglied = ersatzmitglied;
	}

	/**
	 * Nachbedingung: der Rueckgabewert ist ungleich null
	 */
	public String toString() {
		return this.name;
	}

	/**
	 * Nachbedingung: der Rueckgabewert ist ungleich null
	 */
	public String toDetailString() {
		return toString() + " (" + this.instrument + ") " + this.zeitraum
				+ "\n" + "TelefonNr: " + this.telNr
				+ (ersatzmitglied ? ", Ersatzmitglied" : "");
	}

	/**
	 * NOTE: Gibt entweder Mitglieder die Ersatzmitglieder sind aus, oder jene
	 * die keine sind.
	 * 
	 * @author VHD
	 */
	public static class TypSelector implements Selector<Mitglied> {
		private boolean isE;

		/**
		 * NOTE: Vergleicht den im Parameter uebergebenen Wert mit dem boolschen
		 * Wert fuer Ersatzmitglied.
		 * 
		 * @param isErsatzmitglied
		 *            True gibt nur Ersatzmitglieder zurueck, False hingegen nur
		 *            Stammmitglieder
		 */
		public TypSelector(boolean isErsatzmitglied) {
			isE = isErsatzmitglied;
		}

		@Override
		/**
		 * Vorbedingung: item != null
		 */
		public boolean select(Mitglied item) {
			return item.ersatzmitglied == isE;
		}
	}

	public static class ZeitraumSelektor implements Selector<Mitglied> {
		private Date zeitpunkt;

		/**
		 * Vorbedingung: zeitpunkt != null
		 */
		public ZeitraumSelektor(Date zeitpunkt) {
			this.zeitpunkt = zeitpunkt;
		}

		@Override
		/**
		 * Vorbedingung: item != null
		 */
		public boolean select(Mitglied item) {
			return item.getZeitraum().inZeitraum(zeitpunkt);
		}
	}

	public static class InstrumentSelektor implements Selector<Mitglied> {
		private String instrument;

		public InstrumentSelektor(String instrument) {
			this.instrument = instrument;
		}

		@Override
		/**
		 * Vorbedingung: item != null
		 */
		public boolean select(Mitglied item) {
			return item.instrument.compareToIgnoreCase(instrument) == 0;
		}
	}

	public static class NameSelektor implements Selector<Mitglied> {
		private String[] namen;

		/**
		 * Vorbedingung: namen enthaelt keine Elemente gleich null
		 */
		public NameSelektor(String... namen) {
			this.namen = namen;
		}

		@Override
		/**
		 * Vorbedingung: item ist ungleich null
		 */
		public boolean select(Mitglied item) {
			for (String name : namen) {
				if (name.equalsIgnoreCase(item.name)) {
					return true;
				}
			}

			return false;
		}
	}
}

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

/**
 * NOTE: Speichert Personenspezifische Daten, und kann Nachrichten, sowie Terminvorschlaege empfangen
 * 
 * Invariante: gibt keine NULL Werte zurueck. Stellt nicht sicher, dass Elemente in einer der Queues eingefuegt werden!
 * Vorbedingung: Queues duerfen keine NULL Elemente uebergeben werden
 * 
 * @author Christian Kletzander
 */
public class Mitglied implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//NOTE: Variablendefinition
	private String name;
	private String telNr;
	private String instrument;
	private Zeitraum zeitraum;
	private Queue<String> nachrichten;
	private Queue<Terminvorschlag> terminvorschlaege;
	private boolean ersatzmitglied;

	/**
	 * Vorbedingung: Parameter durfen nicht NULL sein.
	 * Endbedingung: Queues sind leer
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
	 * Nachbedingung: es wird nicht sichergestellt, dass Nachricht tatsaechlich in Queue ist.
	 * 
	 * @param nachricht
	 */
	public void sende(String nachricht) {
		this.nachrichten.offer(nachricht);
	}

	/**
	 * Nachbedingung: es wird nicht sichergestellt, dass Nachricht tatsaechlich in Queue ist.
	 * 
	 * @param terminvorschlag
	 */
	public void sende(Terminvorschlag terminvorschlag) {
		this.terminvorschlaege.offer(terminvorschlag);
	}

	public void revidiere(Terminvorschlag terminvorschlag) {
		this.terminvorschlaege.remove(terminvorschlag);
	}

	public Queue<String> getNachrichten() {
		return nachrichten;
	}

	public Queue<Terminvorschlag> getTerminvorschlaege() {
		return terminvorschlaege;
	}
	
	public void setErsatzmitglied(boolean ersatzmitglied) {
		this.ersatzmitglied = ersatzmitglied;
	}

	public String toString() {
		return this.name;
	}

	public String toDetailString() {
		return toString() + " (" + this.instrument + ") " + this.zeitraum
				+ "\n" + "TelefonNr: " + this.telNr + (ersatzmitglied ? ", Ersatzmitglied" : "");
	}

	/**
	 * NOTE: Gibt entweder Mitglieder die Ersatzmitglieder sind aus, oder jene die
	 * keine sind.
	 * 
	 * Vorbedingung: Parameter durfen nicht NULL sein
	 * 
	 * @author VHD
	 */
	public static class TypSelector implements Selector<Mitglied> {
		private boolean isE;

		/**
		 * Vergleicht den im Parameter uebergebenen Wert mit dem boolschen Wert
		 * fuer Ersatzmitglied.
		 * 
		 * @param isErsatzmitglied
		 *            True gibt nur Ersatzmitglieder zurueck, False hingegen nur
		 *            Stammmitglieder
		 */
		public TypSelector(boolean isErsatzmitglied) {
			isE = isErsatzmitglied;
		}

		@Override
		public boolean select(Mitglied item) {
			return item.ersatzmitglied == isE;
		}
	}

	/**
	 * Vorbedingung: Parameter durfen nicht NULL sein
	 * 
	 * @author Koegler Alexander
	 */
	public static class ZeitraumSelektor implements Selector<Mitglied> {
		private Date zeitpunkt;

		public ZeitraumSelektor(Date zeitpunkt) {
			this.zeitpunkt = zeitpunkt;
		}

		@Override
		public boolean select(Mitglied item) {
			return item.getZeitraum().inZeitraum(zeitpunkt);
		}

	}

	/**
	 * Vorbedingung: Parameter durfen nicht NULL sein
	 * 
	 * @author Koegler Alexander
	 */
	public static class InstrumentSelektor implements Selector<Mitglied> {
		private String instrument;

		public InstrumentSelektor(String instrument) {
			this.instrument = instrument;
		}

		@Override
		public boolean select(Mitglied item) {
			return item.instrument.compareToIgnoreCase(instrument) == 0;
		}
	}

	/**
	 * Vorbedingung: Parameter durfen nicht NULL sein
	 * 
	 * @author Koegler Alexander
	 */
	public static class NameSelektor implements Selector<Mitglied> {
		private String[] namen;

		public NameSelektor(String... namen) {
			this.namen = namen;
		}

		@Override
		public boolean select(Mitglied item) {
			for(String name : namen) {
				if(name.equalsIgnoreCase(item.name)) {
					return true;
				}
			}
			
			return false;
		}
	}
}

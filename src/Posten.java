import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

/**
 * 
 * @author Christian Kletzander
 * Invariante: keine NULL Werte erden zurueckgeliefert.
 */

public class Posten implements Serializable {

	private static final long serialVersionUID = 1L;

	private double einnahmen;
	private double ausgaben;
	private String bezeichnung;
	private Date datum;

	/**NOTE: Legt neuen Posten an.
	 * @param einnahmen
	 * @param ausgaben
	 * @param bezeichnung
	 * @param datum
	 * Vorbedingung: Parameter duerfen nicht NULL sein. double Werte muessen >= 0 sein.
	 */
	public Posten(double einnahmen, double ausgaben, String bezeichnung,
			Date datum) {

		this.einnahmen = einnahmen;
		this.ausgaben = ausgaben;
		this.bezeichnung = bezeichnung;
		this.datum = datum;

	}

	/**
	 * Selektiert Posten die sich mit angegebenen Zeitraum ueberschneiden
	 */
	public static class ZeitraumSelektor implements Selector<Posten> {

		private Zeitraum zeitraum;

		/**
		 * 
		 * @param zeitraum
		 * Vorbedingung: Parameter darf nicht NULL sein
		 */
		public ZeitraumSelektor(Zeitraum zeitraum) {
			this.zeitraum = zeitraum;
		}

		/**
		 * Vorbedingung: Parameter darf nicht NULL sein
		 */
		@Override
		public boolean select(Posten item) {
			return this.zeitraum.inZeitraum(item.datum);
		}

	}

	/**
	 * 
	 * @return Einnahmen
	 * Nachbedingung: gibt Zahl >= 0 zurueck
	 */
	public double getEinnahmen() {
		return this.einnahmen;
	}

	/**
	 * 
	 * @return Ausgaben
	 * 
	 * Nachbedingung: gibt Zahl >= 0 zurueck
	 */
	public double getAusgaben() {
		return this.ausgaben;
	}

	public String toString() {
		String gewinn = String.format("%+,.2f", (einnahmen - ausgaben));
		return this.bezeichnung + ": " + gewinn;
	}

	public String toDetailString() {
		String datum = DateFormat.getDateInstance().format(this.datum);
		String einAus = String.format("+%,.2f/-%,.2f", einnahmen, ausgaben);
		return datum + ": " + this.bezeichnung + " (" + einAus + ")";
	}
}

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

/**
 * Invariante: bezeichnung und datum sind ungleich null.
 * 
 * Invariante: einnahmen und ausgaben sind >= 0
 * 
 * @author Christian Kletzander
 */

public class Posten implements Serializable {

	private static final long serialVersionUID = 1L;

	private double einnahmen;
	private double ausgaben;
	private String bezeichnung;
	private Date datum;

	/**
	 * NOTE: Legt neuen Posten an.
	 * 
	 * Vorbedingung: bezeichnung und datum sind ungleich null. einnahmen und
	 * ausgaben sind >= 0
	 * 
	 * @param einnahmen
	 * @param ausgaben
	 * @param bezeichnung
	 * @param datum
	 */
	public Posten(double einnahmen, double ausgaben, String bezeichnung,
			Date datum) {

		this.einnahmen = einnahmen;
		this.ausgaben = ausgaben;
		this.bezeichnung = bezeichnung;
		this.datum = datum;

	}

	/**
	 * NOTE: Selektiert Posten die sich mit angegebenen Zeitraum ueberschneiden
	 */
	public static class ZeitraumSelektor implements Selector<Posten> {

		private Zeitraum zeitraum;

		/**
		 * Vorbedingung: zeitraum ist ungleich null
		 * 
		 * @param zeitraum
		 */
		public ZeitraumSelektor(Zeitraum zeitraum) {
			this.zeitraum = zeitraum;
		}

		/**
		 * Vorbedingung: item ist ungleich null
		 */
		@Override
		public boolean select(Posten item) {
			return this.zeitraum.inZeitraum(item.datum);
		}

	}

	/**
	 * Nachbedingung: der Rueckgabewert ist >= 0
	 */
	public double getEinnahmen() {
		return this.einnahmen;
	}

	/**
	 * Nachbedingung: der Rueckgabewert ist >= 0
	 */
	public double getAusgaben() {
		return this.ausgaben;
	}

	/**
	 * Nachbedingung: der Rueckgabewert ist ungleich null
	 */
	public String toString() {
		String gewinn = String.format("%+,.2f", (einnahmen - ausgaben));
		return this.bezeichnung + ": " + gewinn;
	}

	/**
	 * Nachbedingung: der Rueckgabewert ist ungleich null
	 */
	public String toDetailString() {
		String datum = DateFormat.getDateInstance().format(this.datum);
		String einAus = String.format("+%,.2f/-%,.2f", einnahmen, ausgaben);
		return datum + ": " + this.bezeichnung + " (" + einAus + ")";
	}
}

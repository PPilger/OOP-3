import java.util.List;

/**
 * Invariante: band ist ungleich NULL
 * 
 * @author Christian Kletzander
 */

public class GuV extends Selection<Posten> {
	private static final long serialVersionUID = 1L;

	private Band band;

	/**
	 * NOTE: Erstellt ein neues Objekt GuV fuer die Klasse Band.
	 * 
	 * Vorbedingung: band ist ungleich NULL
	 * 
	 * @param band
	 *            - Ein Band Objekt fuer das dieses GuV Objekt erstellt wird.
	 */
	public GuV(Band band) {
		this.band = band;
	}

	/**
	 * NOTE: Erstelle eine neue Termin Sammlung die auf den selben Daten wie
	 * <code>base</code> arbeitet. Es sind jedoch nur Elemente sichtbar, die von
	 * den Selektoren selektiert werden.
	 * 
	 * Vorbedingung: band, base und selectors sind ungleich NULL, selectors
	 * enthaelt keine Elemente gleich NULL
	 * 
	 * @param base
	 *            Das auszugehende GuV Objekt.
	 * @param selectors
	 *            Selektoren, die Suchbedingungen setzen.
	 */
	private GuV(Band band, GuV base, List<Selector<Posten>> selectors) {
		super(base, selectors);
		this.band = band;
	}

	/**
	 * NOTE: Liefert eine Selektion der in diesem Objekt gespeicherten Posten.
	 * Mit den uebergebenen Selektoren kann bestimmt werden, welche Posten
	 * selektiert werden. Aenderungen in der zurueckgegebenen Selektion wirken
	 * sich direkt auf das Original aus.
	 * 
	 * Vorbedingung: selectors ist ungleich NULL und enthaelt keine Elemente
	 * gleich NULL
	 * 
	 * @param selectors
	 *            - Selektoren, die Suchbedingungen setzen.
	 * @return - Gibt ein GuV-Objekt mit gesetzten Filtern zurueck.
	 */
	public GuV select(List<Selector<Posten>> selectors) {
		return new GuV(band, this, selectors);
	}

	/**
	 * NOTE: Berechnet die Summe der Einnahmen aller selektierter Posten. Daber
	 * werden auch Posten der Termine beruecksichtigt.
	 * 
	 * Nachbedingung: der Rueckgabewert ist >= 0
	 * 
	 * @return Summe der Einnahmen der selektierten Posten
	 */
	public double getEinnahmen() {
		double einnahmen = 0;

		/*
		 * NOTE: Durchiterieren der gespeicherten Posten. Falls keine Posten
		 * vorhanden sind, wird dieser Block uebersprungen.
		 */
		for (Posten posten : this) {
			einnahmen += posten.getEinnahmen();
		}

		// Zusicherung: einnahmen enthaelt die Summe der Einnahmen aller
		// selektierten Posten (ohne Termine)

		/*
		 * NOTE: Durchiterieren aller Termine der Band. Falls keine Termine
		 * vorhanden sind, wird dieser Block uebersprungen.
		 */
		for (Termin termin : band.getTermine()) {
			Posten posten = termin.getPosten();
			if (selected(posten)) {
				einnahmen += posten.getEinnahmen();
			}
		}

		return einnahmen;
	}

	/**
	 * NOTE: Berechnet die Summe der Ausgaben aller selektierter Posten. Daber
	 * werden auch Posten der Termine beruecksichtigt.
	 * 
	 * Nachbedingung: der Rueckgabewert ist >= 0
	 * 
	 * @return Summe der Ausgaben der selektierten Posten
	 */
	public double getAusgaben() {
		double ausgaben = 0;

		/*
		 * NOTE: Durchiterieren der gespeicherten Posten Falls keine Posten
		 * vorhanden sind, wird dieser Block uebersprungen.
		 */
		for (Posten posten : this) {
			ausgaben += posten.getAusgaben();
		}

		// Zusicherung: ausgaben enthaelt die Summe der Ausgaben aller
		// selektierten Posten (ohne Termine)

		/*
		 * NOTE: Durchiterieren aller Termine der Band Falls keien Termine
		 * vorhanden sind, wird dieser Block uebersprungen.
		 */
		for (Termin termin : band.getTermine()) {
			Posten posten = termin.getPosten();
			if (selected(posten)) {
				ausgaben += posten.getAusgaben();
			}
		}

		return ausgaben;
	}

	/**
	 * NOTE: Berechnet den Gesamtgewinn aller selektierter Posten. Daber werden
	 * auch Posten der Termine beruecksichtigt.
	 * 
	 * @return Gesamtgewinn der selektierten Posten
	 */
	public double getGewinn() {
		double ausgaben = 0;

		/*
		 * NOTE: Durchiterieren der gespeicherten Posten Falls keien Posten
		 * vorhanden sind, wird dieser Block uebersprungen.
		 */
		for (Posten posten : this) {
			ausgaben += posten.getEinnahmen() - posten.getAusgaben();
		}

		// Zusicherung: ausgaben enthaelt den Gesamtgewinn aller
		// selektierter Posten (ohne Termine)

		/*
		 * NOTE: Durchiterieren aller Termine der Band Falls keine Termine
		 * vorhanden sind, wird dieser Block uebersprungen.
		 */
		for (Termin termin : band.getTermine()) {
			Posten posten = termin.getPosten();
			if (selected(posten)) {
				ausgaben += posten.getEinnahmen() - posten.getAusgaben();
			}
		}

		return ausgaben;
	}
}

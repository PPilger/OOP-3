/**
 * NOTE:Repraesentiert einen Song in einer bestimmten Variante
 * 
 * Invariante: song und variante sind ungleich null
 * 
 * @author Peter Pilgerstorfer
 */
public class SongVariante {
	private Song song;
	private Variante variante;

	/**
	 * NOTE: Verknuepft eine bestehende Variante mit Song
	 * 
	 * Vorbedingung: song und variante sind ungleich null
	 * 
	 * @param song
	 * @param variante
	 */
	public SongVariante(Song song, Variante variante) {
		this.song = song;
		this.variante = variante;
	}

	/**
	 * Nachbedingung: der Rueckgabewert ist ungleich null
	 */
	public Song getSong() {
		return song;
	}

	/**
	 * Nachbedingung: der Rueckgabewert ist ungleich null
	 */
	public Variante getVariante() {
		return variante;
	}

	/**
	 * Nachbedingung: der Rueckgabewert ist ungleich null
	 */
	public String toString() {
		return song + " (" + variante + ")";
	}
}

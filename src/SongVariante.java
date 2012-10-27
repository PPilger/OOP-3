/**
 * Repraesentiert einen Song in einer bestimmten Variante
 * 
 * @author Peter Pilgerstorfer
 * Invariante: keine NULL Werte werden zurueck geliefert
 */
public class SongVariante {
	private Song song;
	private Variante variante;

	/**
	 * Verknuepft eine bestehende Variante mit Song
	 * @param song
	 * @param variante
	 * Vorbedinung: Parameter duerfen nicht NULL sein
	 */
	public SongVariante(Song song, Variante variante) {
		this.song = song;
		this.variante = variante;
	}

	public Song getSong() {
		return song;
	}

	public Variante getVariante() {
		return variante;
	}

	public String toString() {
		return song + " (" + variante + ")";
	}
}

/**
 * NOTE:Repraesentiert einen Song in einer bestimmten Variante
 * 
 * @author Peter Pilgerstorfer
 * 
 */
public class SongVariante {
	private Song song;
	private Variante variante;

	/**
	 * NOTE: Verknuepft eine bestehende Variante mit Song
	 * @param song
	 * @param variante
	 * Vorbedinung: Parameter duerfen nicht NULL sein
	 */
	public SongVariante(Song song, Variante variante) {
		this.song = song;
		this.variante = variante;
	}

	/**
	 * Invariante: keine NULL Werte werden zurueck geliefert
	 */
	public Song getSong() {
		return song;
	}

	/** 
	 * @return
	 * Invariante: keine NULL Werte werden zurueck geliefert
	 */
	public Variante getVariante() {
		return variante;
	}

	public String toString() {
		return song + " (" + variante + ")";
	}
}

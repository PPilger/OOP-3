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
	 * 
	 * Vorbedinung: Parameter duerfen nicht NULL sein
	 * 
	 * @param song
	 * @param variante
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
	 * Invariante: keine NULL Werte werden zurueck geliefert
	 * 
	 * @return
	 */
	public Variante getVariante() {
		return variante;
	}

	public String toString() {
		return song + " (" + variante + ")";
	}
}

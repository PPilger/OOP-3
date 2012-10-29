import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * NOTE: Eine Selektion von Elementen. Welche Elemente sichtbar sind, wird mit
 * Selector-Objekten bestimmt.
 * 
 * @author Peter Pilgerstorfer
 * Invariante: Die privaten Listen sind niemals NULL oder enthalten NULL Elemente. Nur getFirst() kann NULL zurueckliefern
 */
public class Selection<T> implements Iterable<T>, Serializable {
	private static final long serialVersionUID = 1L;

	private List<T> list;
	private List<T> removed;
	private transient List<Selector<T>> selectors;

	/**
	 * Nachbedinung: alle privaten Listen sind instanziert
	 */
	public Selection() {
		this.list = new ArrayList<T>();
		this.removed = new ArrayList<T>();
		this.selectors = new ArrayList<Selector<T>>();
	}

	/**
	 * NOTE: Erstelle eine neue Sicht die auf den selben Daten wie <code>base</code>
	 * arbeitet. Die uebergebenen Selektoren werden zusaetzlich zu den in
	 * <code>base</code> bestehenden uebernommen.
	 *  
	 * @param base
	 * @param selectors
	 * Vorbedingung: Parameter duerfen nicht NULL sein, und keine Elemente enhalten die NULL sind
	 */
	public Selection(Selection<T> base, List<Selector<T>> selectors) {
		this.list = base.list;
		this.removed = base.removed;
		this.selectors = selectors;
		this.selectors.addAll(base.selectors);
	}

	/**
	 * @return das erste selektierte Element
	 * Nachbedingung: gibt ein Element vom Typ T zurueck das nicht NULL ist; oder gibt NULL zurueck, falls keine Elemente in Liste enthalten sind
	 */
	public T getFirst() {
		Iterator<T> iter = iterator();
		if (iter.hasNext()) {
			return iterator().next();
		}
		return null;
	}

	/**
	 * @return die aktuelle Selektion als Liste.
	 * Nachbedingung: Liefert Liste die Elemente, ohne NULL Elemente, enthaelt oder leere Liste vom Typ T
	 */
	public List<T> asList() {
		List<T> list = new ArrayList<T>();

		for (T element : this) {
			list.add(element);
		}

		return list;
	}

	/**
	 * NOTE: Fuegt ein Element zur Liste hinzu.
	 * 
	 * Vorbedingung: Parameter darf nicht NULL sein.
	 * @param element
	 */
	public boolean add(T element) {
		return list.add(element);
	}

	/**
	 * NOTE: Entfernt alle selektierten Elemente.
	 * 
	 * @return die Anzahl der entfernten Elemente
	 * Nachbedingung: liefert int Wert >= 0 zurueck
	 */
	public int remove() {
		int removed = 0;

		Iterator<T> iter = iterator();
		while (iter.hasNext()) {
			iter.next();
			iter.remove();
			removed++;
		}

		return removed;
	}

	/**
	 * NOTE: Stellt alle selektierten, geloeschten Elemente wieder her.
	 * 
	 * Nachbedingung: fuegt kein oder mehrere Elemente in die aktuelle Liste hinzu
	 */
	public void restore() {
		Iterator<T> removedIter = removedIterator();

		while (removedIter.hasNext()) {
			T element = removedIter.next();
			removedIter.remove();
			list.add(element);
		}
	}

	/**
	 * @return Anzahl der selektierten Elemente
	 * Nachbedingung: liefert int >= 0 zurueck
	 */
	public int count() {
		int count = 0;
		Iterator<T> iter = iterator();

		while (iter.hasNext()) {
			iter.next();
			count++;
		}

		return count;
	}

	/**
	 * NOTE: Vorbedingung: Parameter darf nicht NULL sein
	 * @param element
	 * @return true, wenn alle Selektoren das Element selektieren, false
	 *         anderenfalls.
	 */
	public boolean selected(T element) {
		for (Selector<T> selector : selectors) {
			if (!selector.select(element)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * NOTE: Gibt die Selection im Format einer gewoehnlichen java.util.Collection
	 * zurueck.
	 * Nachbedingung: liefert gueltiges String-Objekt zurueck
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		Iterator<T> iter = iterator();

		builder.append('[');

		if (iter.hasNext()) {
			builder.append(iter.next());
		}
		while (iter.hasNext()) {
			builder.append(", ");
			builder.append(iter.next());
		}

		builder.append(']');

		return builder.toString();
	}

	/**
	 * NOTE: Initialisiert die Selektoren, da Selektoren nicht serialisierbar sind.
	 * 
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		selectors = new ArrayList<Selector<T>>();
	}

	/**
	 * NOTE: Erstellt einen neuen Iterator, der alle selektierten Elemente
	 * durchlaeuft.
	 * 
	 * Elemente die von diesem Iterator entfernt werden, koennen mit
	 * <code>restore</code> wiederhergestellt werden.
	 */
	@Override
	public Iterator<T> iterator() {
		return new SelectionIterator(list, removed);
	}

	/**
	 * NOTE: Erstellt einen neuen Iterator, der alle selektierten, geloeschten
	 * Elemente durchlaeuft.
	 * 
	 * Elemente die von diesem Iterator entfernt werden, sind nicht
	 * wiederherstellbar!
	 */
	public Iterator<T> removedIterator() {
		return new SelectionIterator(removed);
	}

	/**
	 * NOTE: Repraesentiert einen Iterator, der durch alle selektierten Elemente
	 * iteriert.
	 * 
	 * @author Peter Pilgerstorfer
	 * 
	 */
	private class SelectionIterator implements Iterator<T> {
		private Collection<T> removed; // Collection aller entfernten Elemente
		private ListIterator<T> sourceIter; // der Iterator ueber alle Elemente
		private T current; // das aktuelle Element
		private T next; // das naechste selektierte Element, bzw. null am Ende

		/**
		 * 
		 * @param source
		 * Vorbedingung: Listen darf keine NULL Elemente enthalten, oder NULL sein
		 */
		public SelectionIterator(List<T> source) {
			this(source, null);
		}

		/**
		 * @param source
		 * @param removed
		 *            die Collection wo entfernte Elemente gespeichert werden
		 *  Vorbedingung: Listen duerfen keine NULL Elemente enthalten, oder NULL sein (ausg. removed)
		 */
		public SelectionIterator(List<T> source, Collection<T> removed) {
			this.sourceIter = source.listIterator();
			this.current = null;
			this.next = nextSelected();
			this.removed = removed;
		}

		/**
		 * @return das naechste selektierte Element
		 * 
		 * Nachbedingung: liefert NULL zurueck oder das naechste Element des Iterator Aufrufs
		 */
		private T nextSelected() {
			while (sourceIter.hasNext()) {
				T next = sourceIter.next();

				if (selected(next)) {
					return next;
				}
			}

			return null;
		}

		@Override
		public boolean hasNext() {
			return next != null;
		}

		@Override
		/**
		 * Nachbedingung: liefert Element vom Typ T, oder NULL zurueck
		 */
		public T next() {
			current = next;
			next = nextSelected();

			return current;
		}

		@Override
		/**
		 * NOTE: Loescht Element
		 * Nachbedingung: Element wiederherstellbar, sofern removed Liste nicht null ist
		 *
		 */
		public void remove() {
			T previous = null;

			// NOTE: Der Iterator steht schon auf der Position des naechsten
			// Elementes. Daher muss zuerst wieder zur aktuellen Position
			// zurueckgekehrt werden.
			while (sourceIter.hasPrevious() && previous != current) {
				previous = sourceIter.previous();
			}

			sourceIter.remove();

			// NOTE: Der Iterator wird wieder aufs naechste Element gesetzt
			nextSelected();

			if (removed != null) {
				removed.add(current);
			}
		}
	}
}

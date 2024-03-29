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
 * GOOD: Schwache Objektkopplung, starker Klassenzusammenhalt, gute Wartbarkeit.
 * Die Klasse hat verwendet kaum andere Klassen/Methoden des Programms
 * (lediglich Selector). Sie enthaelt ausschliesslich Methoden, die die
 * Selection selbst betreffen. Durch die Vererbung koennen alle Unterklassen
 * trotzdem auf die Funktionalitaet (remove/restore/add/...) zurueckgreifen.
 * 
 * Invariante: list, removed und selectors sind ungleich null und enthalten
 * keine Elemente gleich null.
 * 
 * @author Peter Pilgerstorfer
 * 
 */
public class Selection<T> implements Iterable<T>, Serializable {
	private static final long serialVersionUID = 1L;

	private List<T> list;
	private List<T> removed;
	private transient List<Selector<T>> selectors;

	/**
	 * Nachbedinung: Die Selektion enthaelt keine Elemente.
	 */
	public Selection() {
		this.list = new ArrayList<T>();
		this.removed = new ArrayList<T>();
		this.selectors = new ArrayList<Selector<T>>();
	}

	/**
	 * NOTE: Erstelle eine neue Sicht die auf den selben Daten wie
	 * <code>base</code> arbeitet. Die uebergebenen Selektoren werden
	 * zusaetzlich zu den in <code>base</code> bestehenden uebernommen.
	 * 
	 * Vorbedingung: base und selectors sind ungleich null. selectors enthaelt
	 * keine Elemente gleich null.
	 * 
	 * Nachbedinung: Die neue Selektion arbeitet auf den selben Elementen wie
	 * base. Es sind jedoch nur Elemente sichtbar, die mit den selectors
	 * selektiert werden.
	 * 
	 * @param base
	 * @param selectors
	 */
	public Selection(Selection<T> base, List<Selector<T>> selectors) {
		this.list = base.list;
		this.removed = base.removed;
		this.selectors = selectors;
		this.selectors.addAll(base.selectors);
	}

	/**
	 * @return das erste selektierte Element
	 */
	public T getFirst() {
		Iterator<T> iter = iterator();
		if (iter.hasNext()) {
			return iterator().next();
		}
		return null;
	}

	/**
	 * Nachbedingung: der Rueckgabewert ist eine Liste gefuellt mit allen
	 * selektierten Elementen.
	 * 
	 * @return die aktuelle Selektion als Liste.
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
	 * Vorbedingung: element ist ungleich null.
	 * 
	 * Nachbedingung: Wenn der Rueckgabewert true ist, wurde element zur Liste
	 * hinzugefuegt.
	 * 
	 * @param element
	 */
	public boolean add(T element) {
		return list.add(element);
	}

	/**
	 * NOTE: Entfernt alle selektierten Elemente.
	 * 
	 * Nachbedingung: Rueckgabewert ist >= 0 Nachbedingung: Alle selektierten
	 * Elemente sind entfernt.
	 * 
	 * @return die Anzahl der entfernten Elemente
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
	 * Nachbedingung: alle selektierten, entfernten Elemente sind
	 * wiederhergestellt.
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
	 * Nachbedingung: Rueckgabewert ist >= 0
	 * 
	 * @return Anzahl der selektierten Elemente
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
	 * Vorbedingung: element ist ungleich null
	 * 
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
	 * NOTE: Gibt die Selection im Format einer gewoehnlichen
	 * java.util.Collection zurueck. Nachbedingung: liefert gueltiges
	 * String-Objekt zurueck
	 * 
	 * Nachbedingung: Der Rueckgabewert ist ungleich null.
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
	 * NOTE: Initialisiert die Selektoren, da Selektoren nicht serialisierbar
	 * sind.
	 * 
	 * Nachbedingung: Alle Objekte sind geladen, es sind jedoch keine Selektoren
	 * vorhanden.
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
	 * Nachbedingung: Der Rueckgabewert ist ungleich null.
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
	 * Nachbedingung: Der Rueckgabewert ist ungleich null.
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
	 * Invariante: sourceIter ist ungleich null Invariante: next zeigt auf das
	 * naechste selektierte Element Invariante: current zeigt auf das aktuelle
	 * Element
	 * 
	 * @author Peter Pilgerstorfer
	 */
	private class SelectionIterator implements Iterator<T> {
		private Collection<T> removed; // Collection aller entfernten Elemente
		private ListIterator<T> sourceIter; // der Iterator ueber alle Elemente
		private T current; // das aktuelle Element
		private T next; // das naechste selektierte Element, bzw. null am Ende

		/**
		 * Vorbedingung: source ist ungleich null. source enthaelt keine
		 * Elemente gleich null.
		 * 
		 * @param source
		 */
		public SelectionIterator(List<T> source) {
			this(source, null);
		}

		/**
		 * Vorbedingung: source ist ungleich null. source und removed enthalten
		 * keine Elemente gleich null.
		 * 
		 * @param source
		 * @param removed
		 *            die Collection wo entfernte Elemente gespeichert werden
		 */
		public SelectionIterator(List<T> source, Collection<T> removed) {
			this.sourceIter = source.listIterator();
			this.current = null;
			this.next = nextSelected();
			this.removed = removed;
		}

		/**
		 * @return das naechste selektierte Elemene
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
		 * Vorbedingung: die Methode hasNext() muss true liefern.
		 * Nachbedingung: der Rueckgabewert ist ungleich null.
		 * Nachbedingung: der Iterator steht am naechsten selektierten Element.
		 */
		public T next() {
			current = next;
			next = nextSelected();

			return current;
		}

		@Override
		/**
		 * NOTE: Loescht Element
		 * 
		 * Vorbedingung: next() muss mindestens einmal ausgefuert worden sein.
		 * Vorbedingung: die Methode remove darf fuer das aktuelle Element noch nicht ausgefuert worden sein.
		 * Vorbedingung: die zugrundeliegende Liste darf seit dem letzten Aufruf von next() nicht veraendert worden sein.
		 * Nachbedingung: das Element ist entfernt.
		 */
		public void remove() {
			T previous = null;

			// NOTE: Der Iterator steht schon auf der Position des naechsten
			// Elementes. Daher muss zuerst wieder zur aktuellen Position
			// zurueckgekehrt werden.
			while (sourceIter.hasPrevious() && previous != current) {
				previous = sourceIter.previous();
			}

			// Zusicherung: sourceIter steht auf dem aktuellen Element (current)

			sourceIter.remove();

			// NOTE: Der Iterator wird wieder aufs naechste Element gesetzt
			nextSelected();

			// Zusicherung: sourceIter steht auf dem naechsten Element (next)

			if (removed != null) {
				removed.add(current);
			}
		}
	}
}

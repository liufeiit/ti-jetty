package com.ovea.jetty.session.serializer.jboss.serial.util;

import gnu.trove.TObjectHashingStrategy;

import com.ovea.jetty.session.serializer.jboss.serial.references.EmptyReference;
import com.ovea.jetty.session.serializer.jboss.serial.references.PersistentReference;

public interface ClassMetaConsts {
	static final int REFERENCE_TYPE_IN_USE = PersistentReference.REFERENCE_SOFT;
	static public final Class[] EMPTY_CLASS_ARRY = new Class[0];
	static public final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
	static final PersistentReference emptyReference = new EmptyReference();
	static public final TObjectHashingStrategy identityHashStrategy = new TObjectHashingStrategy() {
		public int computeHashCode(Object o) {
			return System.identityHashCode(o);
		}

		public boolean equals(Object o1, Object o2) {
			return o1 == o2;
		}
	};

	static public final TObjectHashingStrategy regularHashStrategy = new TObjectHashingStrategy() {
		public int computeHashCode(Object o) {
			return o.hashCode();
		}

		public boolean equals(Object o1, Object o2) {
			return o1.getClass() == o2.getClass() && o1.equals(o2);
		}
	};
}

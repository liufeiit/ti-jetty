package com.ovea.jetty.session.serializer.jboss.serial.objectmetamodel.safecloning;

import com.ovea.jetty.session.serializer.jboss.serial.util.ClassMetaConsts;
import gnu.trove.TObjectIntHashMap;

import java.util.ArrayList;

public class SafeCloningRepository implements ClassMetaConsts {

	public SafeCloningRepository(SafeClone safeClone) {
		this.safeClone = safeClone;
	}

	private SafeClone safeClone;

	TObjectIntHashMap safeToReuse = new TObjectIntHashMap(identityHashStrategy);
	ArrayList reuse = new ArrayList();

	public void clear() {
		reuse.clear();
		safeToReuse.clear();
	}

	public int storeSafe(Object obj) {
		if (safeClone.isSafeToReuse(obj)) {
			int description = safeToReuse.get(obj);

			if (description == 0) {
				safeToReuse.put(obj, safeToReuse.size() + 1);
				description = safeToReuse.size();
				reuse.add(obj);
			}
			return description;
		} else {
			return 0;
		}
	}

	public Object findReference(int reference) {
		Object retobject = reuse.get(reference - 1);
		return retobject;
	}
}

///////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2008, Robert D. Eden All Rights Reserved.
// Copyright (c) 2009, Jeff Randall All Rights Reserved.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
///////////////////////////////////////////////////////////////////////////////

package gnu.trove.impl.sync;

//////////////////////////////////////////////////
// THIS IS A GENERATED CLASS. DO NOT HAND EDIT! //
//////////////////////////////////////////////////

////////////////////////////////////////////////////////////
// THIS IS AN IMPLEMENTATION CLASS. DO NOT USE DIRECTLY!  //
// Access to these methods should be through TCollections //
////////////////////////////////////////////////////////////

import gnu.trove.list.*;
import java.util.RandomAccess;

public class TSynchronizedRandomAccessLongList extends TSynchronizedLongList implements RandomAccess {

	static final long serialVersionUID = 1530674583602358482L;

	public TSynchronizedRandomAccessLongList(TLongList list) {
		super(list);
	}

	public TSynchronizedRandomAccessLongList(TLongList list, Object mutex) {
		super(list, mutex);
	}

	public TLongList subList(int fromIndex, int toIndex) {
		synchronized (mutex) {
			return new TSynchronizedRandomAccessLongList(list.subList(fromIndex, toIndex), mutex);
		}
	}

	/**
	 * Allows instances to be deserialized in pre-1.4 JREs (which do not have
	 * SynchronizedRandomAccessList). SynchronizedList has a readResolve method that
	 * inverts this transformation upon deserialization.
	 */
	private Object writeReplace() {
		return new TSynchronizedLongList(list);
	}
}

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

package gnu.trove.impl.unmodifiable;

//////////////////////////////////////////////////
// THIS IS A GENERATED CLASS. DO NOT HAND EDIT! //
//////////////////////////////////////////////////

////////////////////////////////////////////////////////////
// THIS IS AN IMPLEMENTATION CLASS. DO NOT USE DIRECTLY!  //
// Access to these methods should be through TCollections //
////////////////////////////////////////////////////////////

import gnu.trove.list.*;
import java.util.RandomAccess;

public class TUnmodifiableRandomAccessFloatList extends TUnmodifiableFloatList implements RandomAccess {

	private static final long serialVersionUID = -2542308836966382001L;

	public TUnmodifiableRandomAccessFloatList(TFloatList list) {
		super(list);
	}

	public TFloatList subList(int fromIndex, int toIndex) {
		return new TUnmodifiableRandomAccessFloatList(list.subList(fromIndex, toIndex));
	}

	/**
	 * Allows instances to be deserialized in pre-1.4 JREs (which do not have
	 * UnmodifiableRandomAccessList). UnmodifiableList has a readResolve method that
	 * inverts this transformation upon deserialization.
	 */
	private Object writeReplace() {
		return new TUnmodifiableFloatList(list);
	}
}

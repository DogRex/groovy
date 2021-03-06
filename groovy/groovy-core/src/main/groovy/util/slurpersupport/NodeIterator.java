/*
 * Copyright 2005 John G. Wilson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package groovy.util.slurpersupport;

import java.util.Iterator;

/**
 * @author John Wilson
 *
 */

public abstract class NodeIterator implements Iterator {
private final Iterator iter;
private Object nextNode;

  public NodeIterator(final Iterator iter) {
    this.iter = iter;
    this.nextNode = getNextNode(iter);
  }
  
  public boolean hasNext() {
    return this.nextNode != null;
  }
  
  public Object next() {
    try {
      return this.nextNode;
    } finally {
      this.nextNode = getNextNode(this.iter);
    }
  }
  
  public void remove() {
    throw new UnsupportedOperationException();
  }
  
  protected abstract Object getNextNode(final Iterator iter);
}

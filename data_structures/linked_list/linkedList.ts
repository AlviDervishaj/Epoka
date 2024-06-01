export class LinkedNode<T> {
  public _value: T;
  public _next: LinkedNode<T> | null;

  constructor(value: T) {
    this._value = value;
    this._next = null;
  }

  // get value
  get value() {
    return this._value;
  }

  // set value
  set value(value: T) {
    this._value = value
  }

  // get next
  get next() {
    return this._next;
  }

  // set next
  set next(next: LinkedNode<T> | null) {
    this._next = next;
  }
}

export class LinkedList<T> {
  private _head: LinkedNode<T> | null;
  private _size: number;

  constructor() {
    this._head = null;
    this._size = 0;
  }

  /** Get head of linked list*/
  get head() {
    return this._head;
  }

  /** Set head of linked list*/
  set head(newhead: LinkedNode<T> | null) {
    this._head = newhead;
  }

  /** Get size of linked list*/
  get size() {
    return this._size;
  }

  /** Set Size of Linked List */
  set size(newSize: number) {
    this._size = newSize;
  }

  /** Check if list is empty */
  public isEmpty() {
    return this.size === 0;
  }

  /** Increment list size */
  public incrementSize() {
    this.size++;
  }

  /** Decremenet list size */
  public decrementSize() {
    this.size--;
  }


  /** Add new node directly after head */
  public prepend(value: T): T {
    const node = new LinkedNode<T>(value);
    if (this.isEmpty()) {
      // if empty set new node as the head node of list
      this.head = node;
    }
    // list is not empty, meaning it contains a head node
    else {
      node.next = this.head;
      this.head = node;
    }
    this.incrementSize();
    return value;
  }

  /** Add new Node to the end of the list */
  public append(value: T): T {
    const node: LinkedNode<T> = new LinkedNode<T>(value);
    // list is empty
    // point head node to newly created node
    if (this.isEmpty() || this.head === null) {
      this.head = node;
      this.incrementSize();
      return value;
    }
    // list is not empty
    // last node points to the newly created node
    let current: LinkedNode<T> = this.head;
    while (current.next) {
      current = current.next;
    }
    current.next = node;
    this.incrementSize();
    return value;
  }

  // Remove a Node based on its value
  public remove(value: T): T | null {
    // value can not be found because list is empty or no head node
    if (this.isEmpty() || !this.head) return null;
    // value is in the head Node
    else if (this.head.value === value) {
      const removedNode: LinkedNode<T> = this.head;
      this.head = this.head.next;
      this.decrementSize();
      return removedNode.value;
    }
    else {
      let prev = this.head;
      while (prev.next && prev.next.value !== value) {
        prev = prev.next;
      }
      if (prev.next) {
        const removedNode: LinkedNode<T> = prev.next;
        prev.next = removedNode.next;
        this.decrementSize();
        return removedNode.value;
      }
      else return null;
    }
  }

  // Remove Node from array at a given index
  public removeAt(index: number): T | null {
    // index is not valid, list is empty, or no head Node located
    if (index < 0 || index > this.size || this.isEmpty() || !this.head) return null;
    // index is 0
    else if (index === 0) {
      const removedNode: T = this.head.value;
      this.head = this.head.next;
      this.decrementSize();
      return removedNode;
    }
    // index is valid and greater than 0
    else {
      let prev: LinkedNode<T> = this.head;
      let i = 1;
      while (prev && prev.next && i < index) {
        prev = prev.next
        i++;
      }
      // specify to compiler to process prev.next as LinkedNode type
      const removedNode: LinkedNode<T> = prev.next as LinkedNode<T>;
      prev.next = removedNode.next;
      this.decrementSize();
      return removedNode.value;
    }
  }

  // Remove Node from the end of the list
  public pop(): T | null {
    // if head does not exist or if there are no elements then do nothing
    if (!this.head || this.isEmpty()) return null;
    // if size is 1, list contains only one element that is the head element
    if (this.size === 1) {
      const removedNode: LinkedNode<T> = this.head;
      this.head = null;
      this.decrementSize();
      return removedNode.value;
    }
    let current = this.head;
    let prevNode = this.head;
    while (current.next) {
      prevNode = current;
      current = current.next;
    }
    prevNode.next = null;
    this.decrementSize();
    return current.value;
  }

  // Insert a new Node at a given index
  public insertAt(value: T, index: number): T | null {
    if (index < 0 || index > this.size || this.isEmpty() || this.head === null) return null;
    if (index === 0) {
      this.prepend(value);
      return value;
    }
    // index is bigger than 0
    const node: LinkedNode<T> = new LinkedNode<T>(value);
    let previous: LinkedNode<T> = this.head;
    for (let i = 0; i < index - 1; i++) {
      if (previous.next === null) {
        previous.next = node;
        return value;
      }
      previous = previous.next;
    }
    node.next = previous.next;
    previous.next = node;
    this.incrementSize();
    return value;
  }

  // return size of linked list
  public getSize(): number {
    return this.size;
  }

  // Convert linked list to array 
  public toArray(): Array<T> {
    if (this.isEmpty() || this.head === null) {
      return []
    }
    else {
      let current: LinkedNode<T> | null = this.head;
      const values: Array<T> = [];
      while (current) {
        values.push(current.value);
        current = current.next;
      }
      return values;
    }
  }
}


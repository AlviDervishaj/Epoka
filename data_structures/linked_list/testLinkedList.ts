import { LinkedList, LinkedNode } from "./linkedList";

type T = number;

const linkedList = new LinkedList<T>();


console.log(`Initial Linked List: ${linkedList.toArray()}`);
console.log(`Inserting 4 elements. `);
for (let i = 0; i <= 4; i++) {
  linkedList.append(i);
}
console.log(`After Inserting 4 element: ${linkedList.toArray()}`);



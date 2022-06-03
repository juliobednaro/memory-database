# memory-database
A very simple, single-threaded, in-memory database, which has a limited set of data and transaction commands.
```
class DB {
	set(string name, int value)
	get(string name)
	del(string name)
	count(int value)
	begin()
	commit()
	rollback()
}
```
<h2><b>Data commands</b></h2>
The database should accept the following commands:
<ul>
  <li><b>set(string name, int value)</b>: Set a variable [name] to the value [value].</li>
  <li><b>get(string name)</b>: Return the value stored under the variable [name]. Return NULL if that variable name hasn't been set.</li>
  <li><b>del(string name)</b>: Delete the variable [name]</li>
  <li><b>count(int value)</b>: Return the number of variables equal to [value]. If no values are equal, this should return 0.</li>
</ul>
<h2><b>Transaction commands</b></h2>
The database should accept the following commands:
<ul>
  <li><b>begin()</b>: Open a transactional block</li>
  <li><b>rollback()</b>: Rollback all of the commands from the most recent transactional block.</li>
  <li><b>commit()</b>: Permanently store all of the operations from all presently open transactional blocks.</li>
</ul>

<h2>Examples</h2>

```
db.set(“a”, 10);
db.get(“a”); // returns 10
db.del(“a”);
db.get(“a”); // returns NULL
```
```
db.set(“a”, 10”);
db.set(“b”, 10);
db.count(10); // returns 2
db.count(20); // returns 0
db.del(“a”);
db.count(10); // returns 1
db.set(“b”, 30);
db.count(10); // returns 0
```

```
db.begin();
db.set(“a”, 10);
db.get(“a”);	// returns 10
db.begin();
db.set(“a”, 20);
db.get(“a”);	// returns 20
db.rollback();
db.get(“a”);	// returns 10
db.rollback();
db.get(“a”);	// returns NULL
```

```
db.begin();
db.set(“a”, 30);
db.begin();
db.set(“a”, 40);
db.commit();
db.get(“a”);	// returns 40
db.rollback();	// commit stores all transactions, nothing to rollback
db.get(“a”);	// returns 40
```
```
db.set(“a”, 50);
db.begin();
db.get(“a”);	// returns 50
db.set(“a”, 60);
db.begin();
db.delete(“a);
db.get(“a”);	// returns NULL
db.rollback();
db.get(“a”);	// returns 60
db.commit(); 
db.get(“a”);	// returns 60
```

```
db.set(“a”, 10);
db.begin();
db.count(10);	// returns 1
db.begin();
db.del(“a”);
db.count(10);	// returns 0
db.rollback();
db.count(10);	// returns 1 
```

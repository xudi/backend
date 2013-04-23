package backend;
import java.io.IOException;
import java.io.Serializable;

import backend.Storage;

import backend.BackendType;

class Person implements Serializable {
	private static final long serialVersionUID = 1L;

	private int age;
	
	private String name;
	
	public Person(int age, String name) {
		this.age = age;
		this.name = name;
	}
	
	public int getAge() {
		return this.age;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}

public class Test {
	public static void main(String args[]) throws IOException {
		Storage storage1 = testRedis();
		Storage storage2 = testMemcached();
		
		storage1.set("person1", new Person(19, "in redis"));
		Person oldPerson = (Person) storage1.get("person1");
		System.out.println("redis: person has name: " + oldPerson.getName() + ", age: " + oldPerson.getAge());
		
		storage2.set("person1", new Person(20, "in memcached"));
		oldPerson = (Person) storage2.get("person1");
		System.out.println("memcached: person has name: " + oldPerson.getName() + ", age: " + oldPerson.getAge());
	}
	
	public static Storage testRedis() throws IOException {
		return new Storage(BackendType.Redis);
	}
	
	public static Storage testMemcached() throws IOException {
		return new Storage(BackendType.Memcached);
	}
}
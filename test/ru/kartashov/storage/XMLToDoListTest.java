package ru.kartashov.storage;

import ru.kartashov.parser.XMLParser;

class XMLToDoListTest extends ToDoListTest{
    public XMLToDoListTest() {
        super(new XMLToDoList(new XMLParser(),"./ToDoListTest.xml"));
    }
}
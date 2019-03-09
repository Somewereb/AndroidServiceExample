// BookControl.aidl
package com.hdy.bookservice;

import com.hdy.bookservice.Book;

interface BookControl {

    List<Book> getBookList();

    void  addBookInOut(inout Book book);

    void  addBookOut(out Book book);

    void  addBookIn(in Book book);
}

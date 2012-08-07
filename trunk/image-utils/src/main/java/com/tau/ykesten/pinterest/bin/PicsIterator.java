package com.tau.ykesten.pinterest.bin;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import org.codehaus.jackson.map.ObjectMapper;

public class PicsIterator implements Iterator<Picture> {

	private Iterator<File> filesIterator;
	private ObjectMapper mapper;

	public PicsIterator(Collection<File> filesCollection, ObjectMapper mapper) {
		this.filesIterator = filesCollection.iterator();
		this.mapper = mapper;
	}

	@Override
	public boolean hasNext() {
		return filesIterator.hasNext();
	}

	@Override
	public Picture next() {
		try {
			Picture readValue = mapper.readValue(filesIterator.next(), Picture.class);
			return readValue;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void remove() {
	}

}

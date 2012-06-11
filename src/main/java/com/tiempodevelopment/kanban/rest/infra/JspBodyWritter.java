package com.tiempodevelopment.kanban.rest.infra;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

@Produces("text/html")
public class JspBodyWritter implements MessageBodyWriter<Object>  {
	public long getSize(Object t, Class<?> rawType, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}
	
	public boolean isWriteable(Class<?> rawType, Type genericType, 	Annotation[] annotations, MediaType mediaType) {
		return true;
	}
	
	public void writeTo(Object t, Class<?> rawType, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException {
		entityStream.write("Hello world!".getBytes()); 
	}
}

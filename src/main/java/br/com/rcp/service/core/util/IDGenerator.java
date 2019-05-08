package br.com.rcp.service.core.util;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

@Component
public class IDGenerator implements IdentifierGenerator, Serializable {
	private		static		final		String		upper			= "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private		static		final		String		lower			= upper.toLowerCase(Locale.ROOT);
	private		static		final		String		digits			= "0123456789";
	private		static		final		String 		alphanumeric 	= upper + lower + digits;
	private					final		Random		random;
	private					final		char[]		symbols;
	private					final		char[]		buffer;

	private IDGenerator(int length, Random random, String symbols) {
		if (length < 1) throw new IllegalArgumentException();
		if (symbols.length() < 2) throw new IllegalArgumentException();
		this.random		= Objects.requireNonNull(random);
		this.symbols	= symbols.toCharArray();
		this.buffer		= new char[length];
	}

	private IDGenerator(int length, Random random) {
		this(length, random, alphanumeric);
	}

	public IDGenerator(int length) {
		this(length, new SecureRandom());
	}

	public IDGenerator() {
		this(20);
	}

	@SuppressWarnings("WeakerAccess")
	public String generate() {
		for (int i = 0; i < buffer.length; ++i)
			buffer[i] = symbols[random.nextInt(symbols.length)];
		return new String(buffer);
	}

	@Override
	public String generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
		return generate();
	}
}

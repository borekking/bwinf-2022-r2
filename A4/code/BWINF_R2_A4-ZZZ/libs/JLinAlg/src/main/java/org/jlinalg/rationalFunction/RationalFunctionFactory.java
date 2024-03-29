/*
 * This file is part of JLinAlg (<http://jlinalg.sourceforge.net/>).
 * 
 * JLinAlg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * JLinAlg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with JLinALg. If not, see <http://www.gnu.org/licenses/>.
 */
package org.jlinalg.rationalFunction;

import java.util.Map;
import java.util.Random;

import org.jlinalg.IRingElement;
import org.jlinalg.IRingElementFactory;
import org.jlinalg.InvalidOperationException;
import org.jlinalg.JLinAlgTypeProperties;
import org.jlinalg.RingElementFactory;
import org.jlinalg.polynomial.Polynomial;
import org.jlinalg.polynomial.PolynomialFactory;

/**
 * The factory for rational functions. The factory has to be created for a
 * particular
 * sub-type of {@link IRingElement}, that is the constructors of single elements
 * have to be provided with a factory of this type.
 * <P>
 * Note: if the factory of the base type is not a singelton factory, it must
 * implement {@code java.lang.Object.equal(Object o)} such that if the base type
 * are considered equal, the factories are equal.
 * 
 * @author Andreas Keilhauer
 * @param <BASE>
 */
@JLinAlgTypeProperties(isCompound = true)
public class RationalFunctionFactory<BASE extends IRingElement<BASE>>
		extends RingElementFactory<RationalFunction<BASE>>
{

	private static final long serialVersionUID = 1L;

	/**
	 * the basic empty rational function.
	 */
	private final RationalFunction<BASE> ZERO;

	/**
	 * the rational function "1".
	 */
	private final RationalFunction<BASE> ONE;

	/**
	 * the rational function "-1".
	 */
	private final RationalFunction<BASE> M_ONE;

	private RationalFunctionFactory(IRingElementFactory<BASE> baseFactory)
	{
		super();
		if (RationalFunctionFactoryMap.INSTANCE.containsKey(baseFactory))
			throw new InternalError("Try to recreate the same factory again.");
		BASEFACTORY = baseFactory;
		RationalFunctionFactoryMap.INSTANCE.put(
				(IRingElementFactory) BASEFACTORY,
				(RationalFunctionFactory) this);

		PolynomialFactory<BASE> polynomialFactory = PolynomialFactory
				.getFactory(baseFactory);
		ZERO = new RationalFunction<BASE>(polynomialFactory.zero(),
				polynomialFactory.one(), baseFactory);
		ONE = new RationalFunction<BASE>(polynomialFactory.one(),
				polynomialFactory.one(), baseFactory);
		M_ONE = ONE.negate();
	}

	/**
	 * the factory for the base element of the type of polynomials this factory
	 * produces.
	 */
	final IRingElementFactory<BASE> BASEFACTORY;

	/**
	 * The getter for the factory for the base element of the type of
	 * polynomials this factory produces.
	 * 
	 * @return the factory for the base element
	 */
	public IRingElementFactory<BASE> getBaseFactory()
	{
		return BASEFACTORY;
	}

	@Override
	public RationalFunction<BASE> get(long d)
	{
		return get(BASEFACTORY.get(d));
	}

	@Deprecated
	@Override
	public RationalFunction<BASE> gaussianRandomValue()
	{
		throw new InvalidOperationException("Cannot create random polynomials.");
	}

	@Override
	@Deprecated
	public RationalFunction<BASE> randomValue()
	{
		throw new InvalidOperationException("Cannot create random polynomials.");
	}

	@SuppressWarnings("unchecked")
	@Override
	public RationalFunction<BASE>[] getArray(int size)
	{
		return new RationalFunction[size];
	}

	@SuppressWarnings("unchecked")
	@Override
	public RationalFunction<BASE>[][] getArray(int rows, int columns)
	{
		return new RationalFunction[rows][columns];
	}

	@Override
	public RationalFunction<BASE> one()
	{
		return ONE;
	}

	@Override
	public RationalFunction<BASE> zero()
	{
		return ZERO;
	}

	@Override
	public RationalFunction<BASE> m_one()
	{
		return M_ONE;
	}

	/**
	 * Create a rational function with a given numerator and denominator and a
	 * basefactory
	 * 
	 * @param numerator
	 * @param denominator
	 * @param basefactory
	 * @return the polynomial created from the map.
	 */
	public RationalFunction<BASE> get(Polynomial<BASE> numerator,
			Polynomial<BASE> denominator, IRingElementFactory<BASE> basefactory)
	{
		return new RationalFunction<BASE>(numerator, denominator, basefactory);
	}

	/**
	 * Create a rational function with a given numerator. The denominator will
	 * be set to one.
	 * 
	 * @param numerator
	 * @param basefactory
	 * @return the polynomial created from the map.
	 */
	public RationalFunction<BASE> get(Polynomial<BASE> numerator,
			IRingElementFactory<BASE> basefactory)
	{
		return new RationalFunction<BASE>(numerator, basefactory);
	}

	/**
	 * Create a RationalFunction from a {@link Map}<Integer,? extends
	 * {@link IRingElement}> or an instance of {@link IRingElement}. <BR>
	 * Preferred method: {@link #get(Map, IRingElementFactory)}
	 * 
	 * @param o
	 *            an object which can be translated into a coefficient for this
	 *            polynomial.
	 * @return a polynomial consisting of a single constant: the value of
	 *         <code>o</code>
	 */
	@Override
	public RationalFunction<BASE> get(Object o)
	{
		if (o instanceof IRingElement<?>) {
			return new RationalFunction<BASE>((BASE) o);
		}

		if (o instanceof Map<?, ?>) {
			if (((Map<?, ?>) o).isEmpty()) {
				throw new InvalidOperationException(
						"can not create a polynomial from an empty map");
			}
			BASE baseElement = (BASE) ((Map<?, ?>) o).values().toArray()[0];

			return new RationalFunction<BASE>(new Polynomial<BASE>(
					(Map<Integer, BASE>) o, baseElement.getFactory()),
					baseElement.getFactory());
		}
		return get(BASEFACTORY.get(o));
	}

	@Override
	public RationalFunction<BASE> get(int i)
	{
		return get(BASEFACTORY.get(i));
	}

	@Override
	public RationalFunction<BASE> get(double d)
	{
		return get(BASEFACTORY.get(d));
	}

	/**
	 * @throws InvalidOperationException
	 *             if called.
	 */
	@SuppressWarnings("deprecation")
	@Override
	@Deprecated
	public RationalFunction<BASE> gaussianRandomValue(
			@SuppressWarnings("unused") Random random)
	{
		throw new InvalidOperationException(
				"Cannot create random rational function.");
	}

	/**
	 * @throws InvalidOperationException
	 *             if called.
	 */
	@SuppressWarnings("deprecation")
	@Override
	@Deprecated
	public RationalFunction<BASE> randomValue(
			@SuppressWarnings("unused") Random random)
	{
		throw new InvalidOperationException(
				"Cannot create random rational function.");
	}

	@Override
	@Deprecated
	public RationalFunction<BASE> randomValue(
			@SuppressWarnings("unused") RationalFunction<BASE> min,
			@SuppressWarnings("unused") RationalFunction<BASE> max)
	{
		throw new InvalidOperationException(
				"Cannot create random rational function.");
	}

	/**
	 * @throws InvalidOperationException
	 *             if called.
	 */
	@SuppressWarnings("deprecation")
	@Override
	@Deprecated
	public RationalFunction<BASE> randomValue(
			@SuppressWarnings("unused") Random random,
			@SuppressWarnings("unused") RationalFunction<BASE> min,
			@SuppressWarnings("unused") RationalFunction<BASE> max)
	{
		throw new InvalidOperationException(
				"Cannot create random rational function.");
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!(obj instanceof RationalFunctionFactory)) return false;
		return BASEFACTORY.equals(((RationalFunctionFactory<BASE>) obj)
				.getBaseFactory());
	}

	/**
	 * obtain a factory for a given base type. This function can be called again
	 * for the same base type, but only one factory will be created @see
	 * {@link RationalFunctionFactoryMap}
	 * 
	 * @param <RE>
	 *            the base type for the factory
	 * @param baseFactory
	 * @return a factory for rational function.
	 */
	@SuppressWarnings("unchecked")
	public static <RE extends IRingElement<RE>> RationalFunctionFactory<RE> getFactory(
			IRingElementFactory<RE> baseFactory)
	{
		RationalFunctionFactory<RE> factory = (RationalFunctionFactory<RE>) RationalFunctionFactoryMap.INSTANCE
				.get(baseFactory);
		if (factory == null) {
			factory = new RationalFunctionFactory<RE>(baseFactory);
			RationalFunctionFactoryMap.INSTANCE.put(
					(IRingElementFactory) baseFactory,
					(RationalFunctionFactory) factory);
		}
		return factory;
	}

}

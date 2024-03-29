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

import static org.junit.Assume.assumeTrue;

import java.util.Arrays;
import java.util.Collection;

import org.jlinalg.IRingElement;
import org.jlinalg.complex.Complex;
import org.jlinalg.complex.Complex.ComplexFactory;
import org.jlinalg.doublewrapper.DoubleWrapper;
import org.jlinalg.field_p.FieldPFactoryMap;
import org.jlinalg.rational.Rational;
import org.jlinalg.testutil.RingElementTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author Georg Thimm, Andreas Keilhauer
 */
@RunWith(Parameterized.class)
public class RationalFunctionTest<RE extends IRingElement<RE>>
		extends RingElementTestBase<RationalFunction<RE>>
{
	/**
	 * a prime number from the Woodall series
	 */
	final static String prime1 = "32212254719";

	@Parameters
	public static Collection<Object[]> data()
	{
		Object[][] data_ = {
				{
					RationalFunctionFactory.getFactory(Rational.FACTORY)
				},
				{
					RationalFunctionFactory.getFactory(Complex.FACTORY)
				},
				{
					RationalFunctionFactory.getFactory(DoubleWrapper.FACTORY)
				},
				{
					RationalFunctionFactory.getFactory(FieldPFactoryMap
							.getFactory(Long.valueOf(17L)))
				},
				{
					RationalFunctionFactory.getFactory(FieldPFactoryMap
							.getFactory(prime1))
				}
		};
		return Arrays.asList(data_);
	}

	/**
	 * @param factory
	 *            a factory for a polynomial
	 */
	public RationalFunctionTest(RationalFunctionFactory<RE> factory)
	{
		this.factory = factory;
	}

	private RationalFunctionFactory<RE> factory;

	/**
	 * @see org.jlinalg.testutil.TestBaseInterface#getFactory()
	 */
	@Override
	public RationalFunctionFactory<RE> getFactory()
	{
		return factory;
	}

	@Override
	@Test
	public void testLt_base()
	{
		assumeTrue(!(getFactory().getBaseFactory() instanceof ComplexFactory));
		super.testLt_base();
	}

	@Override
	@Test
	public void testGt_base()
	{
		assumeTrue(!(getFactory().getBaseFactory() instanceof ComplexFactory));
		super.testGt_base();
	}

	@Override
	@Test
	public void testGe_base()
	{
		assumeTrue(!(getFactory().getBaseFactory() instanceof ComplexFactory));
		super.testGe_base();
	}

	@Override
	@Test
	public void testLe_base()
	{
		assumeTrue(!(getFactory().getBaseFactory() instanceof ComplexFactory));
		super.testLe_base();
	}
}
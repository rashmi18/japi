/*
 * Copyright (C) 2009  Christian Hujer
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package test.net.sf.japi.tools.replacer;

import java.util.regex.Pattern;
import net.sf.japi.tools.replacer.SplitIterator;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

/** Test line iteration with a {@link SplitIterator}.
 *
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class LineIterationWithSplitIteratorTest extends LineIterationTestCase {

    /** {@inheritDoc} */
    @Override public SplitIterator createIterator(@NotNull final CharSequence text) {
        return new SplitIterator(text, Pattern.compile("(?<=\\n|\u0085|\u2028|\u2029|\\r(?!\\n))"));
    }

    /** Dummy test for working aroud a bug in IntelliJ IDEA.
     * Without this, old versions of IntelliJ IDEA wouldn't know this class contains tests.
     */
    @SuppressWarnings({"JUnitTestMethodWithNoAssertions"})
    @Test
    public void dummyForIntelliJIDEA() { }
}

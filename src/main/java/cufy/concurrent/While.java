/*
 *	Copyright 2020 Cufy
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	    http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package cufy.concurrent;

import java.util.Objects;

/**
 * A {@link Loop} version of the typical {@code while(condition)} statement.
 *
 * @author LSafer
 * @version 0.1.5
 * @since 0.0.1 ~2019.12.07
 */
public class While extends Loop<While.Code> {
	/**
	 * The function to be applied before each round on the loop to check whether the loop must continue or break.
	 */
	protected final Condition condition;

	/**
	 * Construct a new {@code while} loop with the given arguments.
	 *
	 * @param condition looping condition.
	 * @throws NullPointerException if the given {@code condition} is null.
	 */
	public While(Condition condition) {
		Objects.requireNonNull(condition, "condition");
		this.condition = condition;
	}

	/**
	 * Construct a new {@code while} loop with the given arguments.
	 *
	 * @param condition looping condition.
	 * @param code      first looping code.
	 * @throws NullPointerException if ether the given {@code condition} or {@code code} is null.
	 */
	public While(Condition condition, Code code) {
		Objects.requireNonNull(condition, "null");
		Objects.requireNonNull(code, "code");
		this.append(code);
		this.condition = condition;
	}

	@Override
	protected void loop() {
		while (this.condition.check() && this.next(null))
			;
	}

	/**
	 * A loop code for {@code While} loops. Represents the {@code code block}.
	 */
	@FunctionalInterface
	public interface Code extends Loop.Code<While> {
		@Override
		default void run(While loop, Object item) {
			this.onRun(loop);
		}

		/**
		 * Perform this {@code While} loop code with the given item. Get called when a {@code While} loop is executing
		 * its code and this code is added to its code.
		 *
		 * @param loop the loop that executed this code.
		 * @throws NullPointerException if the given {@code loop} is null.
		 */
		void onRun(While loop);
	}

	/**
	 * A condition check for {@code While} loops. Represents the {@code condition}.
	 */
	@FunctionalInterface
	public interface Condition {
		/**
		 * Determine if the loop should continue iterating or not.
		 *
		 * @return true, to not stop the loop.
		 */
		boolean check();
	}
}

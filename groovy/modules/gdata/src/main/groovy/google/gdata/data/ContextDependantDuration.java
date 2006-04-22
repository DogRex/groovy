/*
 * Created on Apr 21, 2006
 *
 * Copyright 2006 John G. Wilson
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package groovy.google.gdata.data;

public class ContextDependantDuration {
    private final int years;
    private final int months;
    private final long millis;
    
    public ContextDependantDuration(final int years, final int months, final long millis) {
        this.years = years;
        this.months = months;
        this.millis = millis;
    }

    public long getMillis() {
        return this.millis;
    }

    public int getMonths() {
        return this.months;
    }

    public int getYears() {
        return this.years;
    }
    
    public ContextDependantDuration plus(final ContextDependantDuration rhs) {
        return new ContextDependantDuration(this.getYears() + rhs.getYears(), this.getMonths() + rhs.getMonths(), this.getMillis() + rhs.getMillis());
    }
    
    public ContextDependantDuration plus(final Duration rhs) {
        return new ContextDependantDuration(this.getYears(), this.getMonths(), this.getMillis() + rhs.getMillis());
    }
    
    public ContextDependantDuration minus(final ContextDependantDuration rhs) {
        return new ContextDependantDuration(this.getYears() - rhs.getYears(), this.getMonths() - rhs.getMonths(), this.getMillis() - rhs.getMillis());
    }
    
    public ContextDependantDuration minus(final Duration rhs) {
        return new ContextDependantDuration(this.getYears(), this.getMonths(), this.getMillis() - rhs.getMillis());
    }
}

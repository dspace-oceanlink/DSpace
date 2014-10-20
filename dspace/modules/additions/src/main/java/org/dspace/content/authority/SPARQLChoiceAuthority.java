/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.content.authority;

import java.util.Iterator;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import org.dspace.app.util.DCInputsReader;
import org.dspace.app.util.DCInputsReaderException;
import org.dspace.core.SelfNamedPlugin;

/**
 * SPARQLChoiceAuthority source that reads the same input-forms which drive
 * configurable submission.
 *
 * Configuration:
 *   This MUST be configured aas a self-named plugin, e.g.:
 *     plugin.selfnamed.org.dspace.content.authority.ChoiceAuthority = \
 *        org.dspace.content.authority.SPARQLChoiceAuthority
 *
 * It AUTOMATICALLY configures a plugin instance for each <value-pairs>
 * element (within <form-value-pairs>) of the input-forms.xml.  The name
 * of the instance is the "value-pairs-name" attribute, e.g.
 * the element: <value-pairs value-pairs-name="common_types" dc-term="type">
 * defines a plugin instance "common_types".
 *
 * IMPORTANT NOTE: Since these value-pairs do NOT include authority keys,
 * the choice lists derived from them do not include authority values.
 * So you should not use them as the choice source for authority-controlled
 * fields.
 *
 * @author Mini Pillai minipillai@atmire.com
 */
public class SPARQLChoiceAuthority extends SelfNamedPlugin implements ChoiceAuthority
{
    private static String values[] = {
            "sun",
            "mon",
            "tue",
            "wed",
            "thu",
            "fri",
            "sat"
    };

    private static String labels[] = {
            "Sunday",
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday"
    };

    public Choices getMatches(String field, String query, int collection, int start, int limit, String locale)
    {
        int dflt = -1;
        ArrayList<Choice> v = new ArrayList<Choice>();
        //Choice v[] = new Choice[values.length];
        for (int i = 0; i < values.length; ++i)
        {


            if (values[i].startsWith(query))
            {
                dflt = i;
                //v[j] = new Choice(String.valueOf(i), values[i], labels[i]);
                v.add(new Choice(String.valueOf(i), values[i], labels[i]));
            }
        }


        if(v.size() == 0)
        {
            for (int i = 0; i < values.length; ++i)
            {
                v.add(new Choice(String.valueOf(i), values[i], labels[i]));
            }
        }
        Choice[] choices = v.toArray(new Choice[v.size()]);
        return new Choices(choices, 0,v.size(), Choices.CF_AMBIGUOUS, false, dflt);
    }

    public Choices getBestMatch(String field, String text, int collection, String locale)
    {
        for (int i = 0; i < values.length; ++i)
        {
            if (text.equalsIgnoreCase(values[i]))
            {
                Choice v[] = new Choice[1];
                v[0] = new Choice(String.valueOf(i), values[i], labels[i]);
                return new Choices(v, 0, v.length, Choices.CF_UNCERTAIN, false, 0);
            }
        }
        return new Choices(Choices.CF_NOTFOUND);
    }

    public String getLabel(String field, String key, String locale)
    {
        return labels[Integer.parseInt(key)];
    }
}

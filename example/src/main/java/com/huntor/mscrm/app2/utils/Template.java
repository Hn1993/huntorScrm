package com.huntor.mscrm.app2.utils;

/**
 * Created by IDEA
 * User : SL
 * on 2015/5/21 0021
 * 21:26.
 */
/**
 * A fast, lightweight string templating system with zero dependencies.
 *
 * You supply a template String and an array of tokens to replace when calling
 * {@link #apply(String...)}. The order of tokens supplied in the constructor
 * corresponds to the order of strings supplied in apply().
 *
 * The Template will compile your string into an executable stack, which
 * generates output with extreme efficiency; the only string-matching performed
 * is during Template compile, and it processes all tokens in a single,
 * efficient iteration of the template String.
 *
 * This ensures an absolute minimum of processing, and allows large templates
 * with a large number of replacements to scale nicely.
 *
 * Usage:
 *
 * assert
 * new Template("$1, $2!", "$1", "$2")
 * .apply("Hello", "World")
 * .equals("Hello, World!");
 *
 * assert
 * new Template("(<>[])$.toArray(new Object[$.size()])", "<>", "$")
 * .apply("String", "myList")
 * .equals("(String[])myList.toArray(new Object[myList.size()])");
 *
 * @author "James X. Nelson (james@wetheinter.net)"
 *
 */
public class Template extends Stack{

    public Template(String template, String... replaceables) {
        super("");
        compile(template, replaceables);
    }

    /**
     * Applies the current template to the supplied arguments.
     *
     */
    public String apply(String... args) {
        return super.apply(args);
    }

    /**
     * Translates a template string into a stack of .toStringable() nodes.
     */
    private final void compile(String template, String[] replaceables) {
        int numLive = 0;
        // These are the only two arrays created by the Template
        int[] tokenPositions = new int[replaceables.length];
        int[] liveIndices = new int[replaceables.length];

        // Get the first index, if any, of each replaceable token.
        for (int i = replaceables.length; i-- > 0;) {
            int next = template.indexOf(replaceables[i]);
            if (next > -1) { // Record only live tokens (ignore missing replacements)
                liveIndices[numLive++] = i;
                tokenPositions[i] = template.indexOf(replaceables[i]);
            }
        }
        // Try to get off easy
        if (numLive == 0) {
            next = new Stack(template);
            return;
        }
        // Perform a single full sort of live indices
        crossSort(liveIndices, tokenPositions, numLive - 1);

        // Recursively fill our stack
        lexTemplate(this, template, replaceables, liveIndices, tokenPositions, 0, numLive);
    }

    /**
     * Performs the lexing of the template, filling the Template Stack.
     */
    private final void lexTemplate(
            Stack head, String template, String[] replaceables,
            int[] liveIndices, int[] tokenPositions, int curPos, int numLive) {
        // Chop up the template string into nodes.
        int nextIndex = liveIndices[0];// Guaranteed the lowest valid position
        int nextPos = tokenPositions[nextIndex];// token position in template String
        assert nextPos > -1;

        // Pull off the constant string value since last token (might be 0 length)
        String constant = template.substring(curPos, nextPos);
        String replaceable = replaceables[nextIndex];
        // Update our index in the template string
        curPos = nextPos + replaceable.length();
        // Push a new node onto the stack
        Stack tail = head.push(constant, nextIndex);

        // Update our sort so liveIndices[0] points to next replacement position
        int newPosition = template.indexOf(replaceable, nextPos + 1);
        if (newPosition == -1) {
            // A token is exhausted
            if (--numLive == 0) {
                // At the very end, we tack on a tail with any remaining string value
                tail.next = new Stack(curPos == template.length() ? "" : template.substring(curPos));
                return; // The end of the recursion
            }
            // Reusing the same array, just shift values left;
            // We limit our scope to the numLive counter, so no need to copy arrays.
            System.arraycopy(liveIndices, 1, liveIndices, 0, numLive);
        } else {
            // This token has another replacement; we may have to re-sort.
            tokenPositions[nextIndex] = newPosition;
            if (numLive > 1 && newPosition > tokenPositions[liveIndices[1]]) {
                // Only re-sort if the new index isn't still lowest
                int test = 1;
                while (newPosition > tokenPositions[liveIndices[test]]) {
                    // Safe to shift backwards
                    liveIndices[test - 1] = liveIndices[test];
                    if (++test == numLive)
                        break;
                }
                // Wherever the loop ended is where the current fragment must go
                liveIndices[test - 1] = nextIndex;
            }
        }
        // If we didn't return, we must recurse
        lexTemplate(tail, template, replaceables, liveIndices, tokenPositions, curPos, numLive);
    }


    /**
     * A simple adaptation of the quicksort algorithm; the only difference is that
     * the values of the array being sorted are pointers to a separate array.
     *
     * This method is only performed once per compile,
     * and then we just keep the pointers sorted as we go.
     *
     * @param pointers
     *          - The pointers to sort in ascending order
     * @param values
     *          - The values used to determine sort order of pointers
     * @param endIndex
     *          - Max index of pointers to sort (inclusive).
     */
    private static void crossSort(int[] pointers, int[] values, int endIndex) {
        for (int i = 0, j = i; i < endIndex; j = ++i) {
            int ai = pointers[i + 1];
            while (values[ai] < values[pointers[j]]) {
                pointers[j + 1] = pointers[j];
                if (j-- == 0)
                    break;
            }
            pointers[j + 1] = ai;
        }
    }
}


/**
 * This is the base stack object used in the compiled Template.
 *
 * This superclass is used only for the head and tail node, which allows us to
 * limit the number of null checks by ensuring regular nodes never have null pointers.
 *
 * @author "James X. Nelson (james@wetheinter.net)"
 *
 */
class Stack {
    final String prefix;
    Stack next;

    Stack(String prefix) {
        assert prefix != null;
        this.prefix = prefix;
    }

    public String apply(String... values) {
        return prefix + (next == null ? "" : next.apply(values));
    }

    /**
     * Pushes a string constant and a pointer to a token's replacement position
     * onto stack.
     */
    final Stack push(String prefix, int pos) {
        assert next == null;
        next = new StackNode(prefix, pos);
        return next;
    }
}

/**
 * This subclass of Stack is for active nodes of the template which have both
 * a string prefix, and a pointer to a replacement value.
 *
 * Each instance of StackNode performs one direct lookup of a value during
 * .toString()
 *
 * @author "James X. Nelson (james@wetheinter.net)"
 *
 */
final class StackNode extends Stack {

    private final int position;

    StackNode(String prefix, int position) {
        super(prefix);
        assert position >= 0;
        this.position = position;
    }

    @Override
    public final String apply(String... values) {
        return prefix
                + (position < values.length && values[position] != null ? values[position] : "")
                + next.apply(values);
    }

}
package dev.krk.emulator.chipsekiz.loader;

import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

public class Layout {
    private final ImmutableList<Section> sections;

    public static Layout empty() {
        return new Layout(Collections.emptyList());
    }

    public List<Section> getSections() {
        return sections;
    }

    public Layout(Collection<Section> sections) {
        this.sections = ImmutableList.copyOf(sections);
    }

    private static class SectionSize {
        private final int origin;
        private final int size;

        public SectionSize(int origin, int size) {

            this.origin = origin;
            this.size = size;
        }

        public boolean isValid(int memorySize) {
            return origin >= 0 && size > 0 && (origin + size) <= memorySize;
        }
    }

    public boolean isValid(int origin, int programSize, int memorySize) {
        checkArgument(memorySize > 0, "memory size must be greater than zero.");
        checkArgument(programSize > 0, "program size must be greater than zero.");
        checkArgument(origin >= 0, "program size must be positive.");
        checkArgument(origin < memorySize, "origin must be inside the memory.");
        checkArgument(origin + programSize <= memorySize, "program must fit in the memory.");

        // There must not be overlap between sections, including the code section.
        List<SectionSize> allSections =
            sections.stream().map(s -> new SectionSize(s.start(), s.data().length))
                .collect(Collectors.toList());
        allSections.add(new SectionSize(origin, programSize));
        allSections.sort(Comparator.comparingInt(o -> o.origin));

        int prevOrigin = -1;
        int prevSize = -1;
        for (SectionSize section : allSections) {
            if (!section.isValid(memorySize)) {
                return false;
            }

            if (prevOrigin + prevSize > section.origin) {
                return false;
            }
            prevOrigin = section.origin;
            prevSize = section.size;
        }
        return true;
    }
}

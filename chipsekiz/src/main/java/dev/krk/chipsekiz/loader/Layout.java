package dev.krk.chipsekiz.loader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class Layout {
    private final List<Section> sections;

    public static Layout empty() {
        return new Layout(List.of());
    }

    public List<Section> getSections() {
        return sections;
    }

    public Layout(Collection<Section> sections) {
        this.sections = List.copyOf(sections);
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
        if (memorySize <= 0) throw new IllegalArgumentException("memory size must be greater than zero.");
        if (programSize <= 0) throw new IllegalArgumentException("program size must be greater than zero.");
        if (origin < 0) throw new IllegalArgumentException("program size must be positive.");
        if (origin >= memorySize) throw new IllegalArgumentException("origin must be inside the memory.");
        if (origin + programSize > memorySize) throw new IllegalArgumentException("program must fit in the memory.");

        // There must not be overlap between sections, including the code section.
        List<SectionSize> allSections = new ArrayList<>(
            sections.stream().map(s -> new SectionSize(s.start(), s.data().length)).toList());
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

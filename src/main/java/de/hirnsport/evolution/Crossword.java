package de.hirnsport.evolution;

import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.util.Factory;
import java.util.stream.IntStream;

public class Crossword {

    public final static String[] WORDS = {
            "ZEIT", "APFEL", "CAMPUS", "RADIO", "HUND", "STIFT", "MAUS","BUCH", "TOR", "GELD", "BEAMER", "DORF", "OHR", "MUND", "VOGEL", "SEE", "SONNE", "FELD", "TELLER", "GABEL", "MINUTE", "SEKUNDE", "STUNDE", "BOOT","BURG", "DACH", "BUCH", "ZELT", "KABEL", "HEMD", "ROCK", "HOSE",
            "REGEN", "HERBST", "EIS", "ERDE", "GENOM", "HAUS", "KATZE", "EULE", "AUTO", "VORTRAG", "UHR", "NASE", "IGEL", "HASE", "BERG", "VATER", "MUTTER", "KIND", "TISCH", "FISCH", "SCHIFF", "ZUG", "LICHT", "BALL", "PFERD", "STALL", "STEIN", "TUER", "MARS", "VENUS", "PLUTO", "SATURN"};


    public static int COUNT_WORDS = 20;

    protected static Field decodeToField(Genotype<IntegerGene> genotype) {
        Field field = new Field();

        IntegerChromosome chromosome = (IntegerChromosome)genotype.chromosome();
        IntStream.range(0, COUNT_WORDS-1)
            .forEach( i -> {
                int wordIndex = Math.abs(chromosome.get(i).allele()) % WORDS.length;
                int xpos = Math.abs(chromosome.get(i+1).allele()) % Field.MAX_WIDTH;
                int ypos = Math.abs(chromosome.get(i+2).allele()) % Field.MAX_HEIGHT;
                int direction = Math.abs(chromosome.get(i+3).allele()) % Field.DirectionEnum.values().length;
                Field.DirectionEnum d = Field.DirectionEnum.values()[direction];

                String word = WORDS[wordIndex];
                field.putWord(word, xpos, ypos, d);
            }
        );
        return field;
    }

    public static float fitness(Genotype<IntegerGene> chromosome) {
        Field field = decodeToField(chromosome);

        float violations = field.getViolations() + 1;
        float letters = field.getLetters() + 1;
        float multiUsed = field.getMultiUsedChars() + 1;
        float wordCount = field.getWordCount() + 1;

        return ((letters * multiUsed * wordCount) / ((violations * violations * violations)));
    }

    public static void main(String[] args) {
        Factory<Genotype<IntegerGene>> gtf = Genotype.of(IntegerChromosome.of(1, 100, (COUNT_WORDS * 4)));

        final Engine<IntegerGene, Float> engine = Engine.builder(Crossword::fitness, gtf)
                .offspringSelector(
                        new RouletteWheelSelector<>())
                .populationSize(1500)
                .alterers(
                        new Mutator<>(0.1),
                        new MultiPointCrossover<>(0.2),
                        new SinglePointCrossover<>(0.3))
                .build();

        Genotype<IntegerGene> result = engine.stream()
                .limit(2500)
                .collect(EvolutionResult.toBestGenotype());

        Field field = decodeToField(result);
        System.out.println(field.printToString());
        System.out.println("Fitness: " + fitness(result));
        System.out.println("Violations:" + (field.getViolations()) + "\nWords:" + field.getWordCount() + "/" + COUNT_WORDS);
        System.out.println("Letters:" + (field.getLetters()) + " used in multiple words:" + field.getMultiUsedChars());

        System.out.println(field.getUsedWords());
    }
}
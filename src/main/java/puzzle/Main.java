package puzzle;

public class Main{
    public static void main(String[] args){
        FileReader reader = new FileReader();
        reader.readInput(java.nio.file.Paths.get("input.txt"));
        if (!reader.validInput){
            System.out.println(reader.errorMsg);
            return;
        }

        Solver solver = new Solver(reader.row, reader.col, reader.blocks);
        solver.solve();
    }
}
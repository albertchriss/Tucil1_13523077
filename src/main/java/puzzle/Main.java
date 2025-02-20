package puzzle;

public class Main{
    public static void main(String[] args){
        FileReader reader = new FileReader();
        reader.readInput(java.nio.file.Paths.get("input.txt"));
        if (!reader.validInput){
            System.out.println(reader.errorMsg);
            return;
        }
        if (reader.mode.equals("DEFAULT")){
            Solver solver = new Solver(reader.row, reader.col, reader.blocks);
            solver.solve();
        }
        else if (reader.mode.equals("CUSTOM")){
            Solver solver = new Solver(reader.row, reader.col, reader.map, reader.blocks);
            solver.solve();
        }
        else{
            // reader.blocks[0].lift();
            // reader.blocks[0].mirrorLift();
            // System.out.println("---------------------------------------");
            // reader.blocks[0].print3D();
            // for (int i = 0; i < reader.numBlocks; i++){
            //     reader.blocks[i].print3D();
            //     System.out.println("---------------------------------------");
            // }
            Solver solver = new Solver(reader.row, reader.col, reader.row, reader.blocks);
            solver.solve();
        }
    }
}
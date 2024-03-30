package database;

public class LoadData {
	public static void main(String[] args) {
		var rs = DB.getRows("select * from problems where id = ? and pw = ?", 1, 3);
		for(var r: DB.getRows("select * from problems")){
			r.get(1);
		}
//		for(var row: DB.getRows("select * from manager_table")) {
//			DB.execute("update manager_table set pw = ?", row.get(3).toString().replaceAll("\r", ""));
//		}
	}
}

package com.usts.tools;

import java.util.ArrayList;

import com.usts.lib.model.NodeInformation;
public class ChangToNode {
	private static int cout = 0;
//	ReadFile readFile = new ReadFile();
	/**
	 * 将readfile类读取的结果集传入
	 * 
	 * @param readResult
	 * @return ArrayList<Node_information>
	 */
	public ArrayList<NodeInformation> changeToNodeInformations(
			ArrayList<String> readResult) {
		ArrayList<NodeInformation> changResult = new ArrayList<NodeInformation>();
		for (int i = 0; i < readResult.size(); i++) {
			NodeInformation node_information = new NodeInformation();
			String[] splitResult = readResult.get(i).split(",");
			node_information.setPid(splitResult[0].replace("\"", ""));
			node_information.setQdz(splitResult[1].replace("\"", ""));
			node_information.setPy(splitResult[2].replace("\"", ""));
			node_information.setZxjd(splitResult[3].replace("\"", ""));
			node_information.setZxwd(splitResult[4].replace("\"", ""));
			node_information.setXjxzqh(splitResult[5].replace("\"", ""));
			node_information.setCqjlx(splitResult[6].replace("\"", ""));
			node_information.setMphpid(splitResult[7].replace("\"", ""));
			node_information.setZhpid(splitResult[8].replace("\"", ""));
			node_information.setMph(splitResult[9].replace("\"", ""));
			node_information.setZh(splitResult[10].replace("\"", ""));
			node_information.setShsj(splitResult[11].replace("\"", ""));
			node_information.setHm(splitResult[12].replace("\"", ""));
			node_information.setDzlx(splitResult[13].replace("\"", ""));
			node_information.setCzbz(splitResult[14].replace("\"", ""));
			changResult.add(node_information);
		}
		return changResult;
	}
	
	public static ArrayList<NodeInformation> convertNodeInformations(ArrayList<String> readResult) {
		ArrayList<NodeInformation> changResult = new ArrayList<NodeInformation>();
		try{
		for (int i = 0; i < readResult.size(); i++) {
			NodeInformation node_information = new NodeInformation();
			String[] splitResult = readResult.get(i).split(",");
			node_information.setPid(splitResult[0].substring(splitResult[0].indexOf(":")+1));
			node_information.setQdz(splitResult[1].substring(splitResult[1].indexOf(":")+1));
			node_information.setPy(splitResult[2].substring(splitResult[2].indexOf(":")+1));
			node_information.setZxjd(splitResult[3].substring(splitResult[3].indexOf(":")+1));
			node_information.setZxwd(splitResult[4].substring(splitResult[4].indexOf(":")+1));
			node_information.setXjxzqh(splitResult[5].substring(splitResult[5].indexOf(":")+1));
			node_information.setCqjlx(splitResult[6].substring(splitResult[6].indexOf(":")+1));
			node_information.setMphpid(splitResult[7].substring(splitResult[7].indexOf(":")+1));
			node_information.setZhpid(splitResult[8].substring(splitResult[8].indexOf(":")+1));
			node_information.setMph(splitResult[9].substring(splitResult[9].indexOf(":")+1));
			node_information.setZh(splitResult[10].substring(splitResult[10].indexOf(":")+1));
			node_information.setShsj(splitResult[11].substring(splitResult[11].indexOf(":")+1));
			node_information.setHm(splitResult[12].substring(splitResult[12].indexOf(":")+1));
			node_information.setDzlx(splitResult[13].substring(splitResult[13].indexOf(":")+1));
			node_information.setCzbz(splitResult[14].substring(splitResult[14].indexOf(":")+1));
			changResult.add(node_information);
		}
		}catch(ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException){
			setCout(getCout() + 1);
		}
		return changResult;
	}

	public static NodeInformation changeToNodeInformations(String str){
		NodeInformation node_information = new NodeInformation();
		try{
				String[] splitResult = str.split(",");
				node_information.setPid(splitResult[0].replace("\"", ""));
				node_information.setQdz(splitResult[1].replace("\"", ""));
				node_information.setPy(splitResult[2].replace("\"", ""));
				node_information.setZxjd(splitResult[3].replace("\"", ""));
				node_information.setZxwd(splitResult[4].replace("\"", ""));
				node_information.setXjxzqh(splitResult[5].replace("\"", ""));
				node_information.setCqjlx(splitResult[6].replace("\"", ""));
				node_information.setMphpid(splitResult[7].replace("\"", ""));
				node_information.setZhpid(splitResult[8].replace("\"", ""));
				node_information.setMph(splitResult[9].replace("\"", ""));
				node_information.setZh(splitResult[10].replace("\"", ""));
				node_information.setShsj(splitResult[11].replace("\"", ""));
				node_information.setHm(splitResult[12].replace("\"", ""));
				node_information.setDzlx(splitResult[13].replace("\"", ""));
				node_information.setCzbz(splitResult[14].replace("\"", ""));
			}catch(ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException){

			}
		return node_information;
	}


	public static int getCout() {
		return cout;
	}

	public static void setCout(int cout) {
		ChangToNode.cout = cout;
	}
}

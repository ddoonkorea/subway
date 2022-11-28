package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

//역 정보
class Station {

	String Station_name;
	int Station_num;
	String line;
	ArrayList<n_s> next_station = new ArrayList<n_s>();

}

//다음역 정보
class n_s extends Station implements Comparable<n_s> {

	int time;
	ArrayList<Transfer> transfer = new ArrayList<Transfer>();

	public n_s() {
	}

	public n_s(String line, int station_num, int time) {
		this.line = line;
		Station_num = station_num;
		this.time = time;
	}

	public int gett() {
		return this.time;
	}

	@Override
	public int compareTo(n_s ns) {

		if (this.time > ns.gett())
			return 1;
		else if (this.time < ns.gett())
			return -1;
		return 0;
	}
}

class Transfer {

	String Station_name;
	String line;
	int time;

	public Transfer(String station_name, String line, int time) {
		Station_name = station_name;
		this.line = line;
		this.time = time;
	}

}


@SuppressWarnings("unused")
public class Main extends Application {
	
	
	// javafx실행
	@Override
	public void start(Stage primaryStage) {
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("root.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			
			primaryStage.setTitle("지하철 길찾기");
			primaryStage.setResizable(true);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 역정보가 담신 해쉬맵
	static HashMap<String, ArrayList<Station>> St = new HashMap<String, ArrayList<Station>>();
	static private final int INF = 100_000_000;

	// 길찾기(다익스트라 알고리즘)
	static n_s dijkstra(Station start, Station end) {

		// 효율적으로 최솟값을 찾기위한 우선순위큐
		PriorityQueue<n_s> pq = new PriorityQueue<n_s>();

		// 방문처리와 시간
		HashMap<String, boolean[]> visit = new HashMap<String, boolean[]>();
		HashMap<String, int[]> time = new HashMap<String, int[]>();

		// 역갯수만큼 공간 생성
		for (Map.Entry<String, ArrayList<Station>> pair : St.entrySet()) {
			visit.put(pair.getKey(), new boolean[pair.getValue().size()]);
			time.put(pair.getKey(), new int[pair.getValue().size()]);
			Arrays.fill(time.get(pair.getKey()), INF);
		}

		// 출발역을 추가하여 시작
		n_s temp = new n_s();
		time.get(start.line)[start.Station_num] = 0;
		temp.Station_name = start.Station_name;
		temp.line = start.line;
		temp.time = time.get(start.line)[start.Station_num];
		temp.Station_num = start.Station_num;

		pq.add(temp);

		// 다익스트라 알고리즘
		while (!pq.isEmpty()) {
			n_s cur = pq.poll();
			if (cur.Station_name.equals(end.Station_name)) {
				return cur;
			}
			if (!visit.get(cur.line)[cur.Station_num]) {
				visit.get(cur.line)[cur.Station_num] = true;

				for (var next : St.get(cur.line).get(cur.Station_num).next_station) {
					if (!visit.get(next.line)[next.Station_num]
							&& time.get(next.line)[next.Station_num] > time.get(cur.line)[cur.Station_num]
									+ next.time) {
						time.get(next.line)[next.Station_num] = time.get(cur.line)[cur.Station_num] + next.time;
						temp = new n_s();
						temp = copy(next, time.get(next.line)[next.Station_num],
								(ArrayList<Transfer>) cur.transfer.clone());
						if (!cur.line.equals(next.line)&&cur.time!=0) {

							temp.transfer.add(new Transfer(St.get(cur.line).get(cur.Station_num).Station_name,
									next.line, time.get(next.line)[next.Station_num]));
						}
						
						
						pq.add(temp);
					}
				}
			}
		}
		return new n_s();
	}

	static n_s copy(n_s co, int time, ArrayList<Transfer> t) {
		n_s temp = new n_s();
		temp.Station_name = St.get(co.line).get(co.Station_num).Station_name;
		temp.line = co.line;
		temp.time = time;
		temp.Station_num = co.Station_num;
		temp.transfer = t;
		return temp;
	}

	// 크롤링
	static void crawling(String URL) {
		// 데이터가 입력된 URL
		try {

			// URL의 html코드 가져오기
			Document document = Jsoup.connect(URL).get();

			// 코드중 원하는 객체 선택
			Elements imageUrlElements = document
					.getElementsByClass("Box-body p-0 blob-wrapper data type-text  gist-border-0");

			// 객체안에 필여한 내용 추출
			Elements subwayElements = document.select("td");

			// 첫줄부터 한줄씩 읽기
			for (int i = 0; i < subwayElements.size(); i++) {
				String subway = subwayElements.get(i).text();
				Station tmp = new Station();

				// 필요한 정보 추출
				String[] temp = subway.split(" ");
				if (temp.length > 3) {
					for (int k = 0; k < temp.length; k++) {
						String[] im = temp[k].split(":");
						if (im.length > 1) {
							if (im[0].equals("station_name")) {
								tmp.Station_name = im[1];
							} else if (im[0].equals("line")) {
								tmp.line = im[1];
							} else if (im[0].equals("station_num")) {
								tmp.Station_num = Integer.parseInt(im[1]) - 1;
							} else if (im[0].equals("next_station")) {
								// (1호선,2,2)(1호선,4,2)
								im[1] = im[1].replace(")(", "-");
								im[1] = im[1].replace("(", "");
								im[1] = im[1].replace(")", "");
								String[] next = im[1].split("-");
								for (String g : next) {
									String[] nim = g.split(",");
									tmp.next_station.add(
											new n_s(nim[0], Integer.parseInt(nim[1]) - 1, Integer.parseInt(nim[2])));
								}

							}
						}
					}
					if (!St.containsKey(tmp.line))
						St.put(tmp.line, new ArrayList<Station>());
					St.get(tmp.line).add(tmp);
				}
			}

		} catch (IOException e) {
		} catch (Exception e) {
			System.out.println("error");
		}

	}

	public static void main(String[] args) {

		String URL = "https://github.com/IMjaeyongpark/2022/blob/main/2-2/Basic%20Project2/project/data";
		crawling(URL);

		launch(args);
	}
}

package com.cinemar.phoneticket.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class Room {

	//podria haber guardado en 1 solo mapa todo pero si despues hago alguna visualizacion separada para los 3 cuerpos supongo que esto va a estar mejor
	private Map<String, Seat> leftSeats = new HashMap<String, Room.Seat>();
	private Map<String, Seat> rightSeats = new HashMap<String, Room.Seat>();;
	private Map<String, Seat> middleSeats = new HashMap<String, Room.Seat>();;

	ArrayList<String> rowsIds = new ArrayList<String>();
	ArrayList<String> colIds = new ArrayList<String>();
	
	private int leftWidth;
	private int rightWidth;
	private int middleWidth;
	
	public int getRowsLength(){
		return rowsIds.size();		
	}
	
	public int getColumnsLength(){
		return colIds.size();		
	}
	
	public Room(JSONObject room) throws JSONException {
		JSONObject left = room.getJSONObject("status").getJSONObject("left");
		JSONObject middle = room.getJSONObject("status")
				.getJSONObject("middle");
		JSONObject right = room.getJSONObject("status").getJSONObject("right");


		parseSeats(left, leftSeats, rowsIds, colIds);
		leftWidth =left.getJSONArray("rows").length();
		parseSeats(middle, middleSeats, rowsIds, colIds);
		middleWidth = middle.getJSONArray("rows").length();		
		parseSeats(right, rightSeats, rowsIds, colIds);
		rightWidth =right.getJSONArray("rows").length();
	}
	
	public Seat getSeat(int row, int col){
		String searchedId = this.translateId(row, col);
		Seat returnedSeat = null; 
		
		returnedSeat = leftSeats.get(searchedId);		
		if (returnedSeat == null) returnedSeat = middleSeats.get(searchedId);
		if (returnedSeat == null) returnedSeat = rightSeats.get(searchedId);
		
		return returnedSeat;
	}

	public String translateId(int row, int col){
		return rowsIds.get(row) + "-" + colIds.get(col);
		
	}
	
	private void parseSeats(JSONObject seatsJsonObject,
			Map<String, Seat> seatContainer, ArrayList<String> rowsIds,
			ArrayList<String> colIds) throws JSONException {
		
		String rowId;
		String colId;
		String id;
		for (int i = 0; i < seatsJsonObject.getJSONArray("rows").length(); i++) {
			for (int j = 0; j < seatsJsonObject.getJSONArray("columns")
					.length(); j++) {
				rowId = seatsJsonObject.getJSONArray("rows").get(i).toString();
				colId = seatsJsonObject.getJSONArray("columns").get(j)
						.toString();
				id = rowId + "-" + colId;

				seatContainer.put(id, new Seat(id));
				
				if (!colIds.contains(colId))
					colIds.add(colId);
				
				if (!rowsIds.contains(rowId)) 
					rowsIds.add(rowId); 
					
				
			}
		}

		for (int i = 0; i < seatsJsonObject.getJSONArray("void_seats").length(); i++) {
			String voidId = seatsJsonObject.getJSONArray("void_seats").get(i).toString();
			seatContainer.get(voidId).setStatus(SeatStatus.NON_EXISTENT);
		}

		for (int i = 0; i < seatsJsonObject.getJSONArray("reserved_seats").length(); i++) {
			String reservedId = seatsJsonObject.getJSONArray("reserved_seats").get(i).toString();
			seatContainer.get(reservedId).setStatus(SeatStatus.OCCUPIED);
		}
	}

	public int getLeftWidth() {
		return leftWidth;
	}

	public int getRightWidth() {
		return rightWidth;
	}

	public int getMiddleWidth() {
		return middleWidth;
	}

	public class Seat {
		private SeatStatus status;
		

		private String id;

		public Seat(String id) {
			this.id = id;
			status = SeatStatus.AVAILABLE;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public SeatStatus getStatus() {
			return status;
		}
		public void setStatus(SeatStatus status) {
			this.status = status;
		}

	}
	
	
}

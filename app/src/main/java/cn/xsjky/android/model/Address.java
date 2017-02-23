package cn.xsjky.android.model;

public class Address extends BaseModel{
	private int AddressId;
	private String Province;
	private String City;
	private String District;
	private String Address;
	private String Longitude;
	private String Latitude;
	private String PostCode;

	public String getLongitude() {
		return Longitude;
	}

	@Override
	public String toString() {
		return "Address{" +
				"AddressId=" + AddressId +
				", Province='" + Province + '\'' +
				", City='" + City + '\'' +
				", District='" + District + '\'' +
				", Address='" + Address + '\'' +
				", Longitude='" + Longitude + '\'' +
				", Latitude='" + Latitude + '\'' +
				", PostCode='" + PostCode + '\'' +
				'}';
	}

	public void setLongitude(String longitude) {
		Longitude = longitude;
	}

	public String getLatitude() {
		return Latitude;
	}

	public void setLatitude(String latitude) {
		Latitude = latitude;
	}

	public String toProvince(){
		return Province + "," + City + "," + District;
	}

	public int getAddressId() {
		return AddressId;
	}

	public void setAddressId(int addressId) {
		AddressId = addressId;
	}

	public String getProvince() {
		return Province;
	}

	public void setProvince(String province) {
		Province = province;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

	public String getDistrict() {
		return District;
	}

	public void setDistrict(String district) {
		District = district;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getPostCode() {
		return PostCode;
	}

	public void setPostCode(String postCode) {
		PostCode = postCode;
	}
	
}

package com.bintang.bangkitcapstoneproject.model

import com.google.gson.annotations.SerializedName

data class RestaurantDetailResponse(

	@field:SerializedName("result")
	val result: RestaurantDetailResult,

	@field:SerializedName("html_attributions")
	val htmlAttributions: List<Any>,

	@field:SerializedName("status")
	val status: String
)

data class RestaurantDetailResult(

	@field:SerializedName("utc_offset")
	val utcOffset: Int,

	@field:SerializedName("formatted_address")
	val formattedAddress: String,

	@field:SerializedName("icon")
	val icon: String,

	@field:SerializedName("rating")
	val rating: Double,

	@field:SerializedName("icon_background_color")
	val iconBackgroundColor: String,

	@field:SerializedName("photos")
	val photos: List<PhotosItem>,

	@field:SerializedName("reference")
	val reference: String,

	@field:SerializedName("user_ratings_total")
	val userRatingsTotal: Int,

	@field:SerializedName("reviews")
	val reviews: List<ReviewsItem>,

	@field:SerializedName("icon_mask_base_uri")
	val iconMaskBaseUri: String,

	@field:SerializedName("adr_address")
	val adrAddress: String,

	@field:SerializedName("place_id")
	val placeId: String,

	@field:SerializedName("types")
	val types: List<String>,

	@field:SerializedName("website")
	val website: String,

	@field:SerializedName("business_status")
	val businessStatus: String,

	@field:SerializedName("address_components")
	val addressComponents: List<AddressComponentsItem>,

	@field:SerializedName("url")
	val url: String,

	@field:SerializedName("price_level")
	val priceLevel: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("opening_hours")
	val openingHours: OpeningHours,

	@field:SerializedName("geometry")
	val geometry: Geometry,

	@field:SerializedName("vicinity")
	val vicinity: String,

	@field:SerializedName("plus_code")
	val plusCode: PlusCode,

	@field:SerializedName("formatted_phone_number")
	val formattedPhoneNumber: String,

	@field:SerializedName("international_phone_number")
	val internationalPhoneNumber: String
)

data class Geometry(

	@field:SerializedName("viewport")
	val viewport: Viewport,

	@field:SerializedName("location")
	val location: Location
)

data class Location(

	@field:SerializedName("lng")
	val lng: Double,

	@field:SerializedName("lat")
	val lat: Double
)

data class Viewport(

	@field:SerializedName("southwest")
	val southwest: Southwest,

	@field:SerializedName("northeast")
	val northeast: Northeast
)

data class Northeast(

	@field:SerializedName("lng")
	val lng: Double,

	@field:SerializedName("lat")
	val lat: Double
)

data class Southwest(

	@field:SerializedName("lng")
	val lng: Double,

	@field:SerializedName("lat")
	val lat: Double
)

data class PlusCode(

	@field:SerializedName("compound_code")
	val compoundCode: String,

	@field:SerializedName("global_code")
	val globalCode: String
)

data class PhotosItem(

	@field:SerializedName("photo_reference")
	val photoReference: String,

	@field:SerializedName("width")
	val width: Int,

	@field:SerializedName("html_attributions")
	val htmlAttributions: List<String>,

	@field:SerializedName("height")
	val height: Int
)

data class OpeningHours(

	@field:SerializedName("open_now")
	val openNow: Boolean,

	//OTHERS OPENING HOURS INFO
	@field:SerializedName("periods")
	val periods: List<PeriodsItem>,

	@field:SerializedName("weekday_text")
	val weekdayText: List<String>
)

//OTHERS RESTAURANT DETAIL INFO
data class Open(

	@field:SerializedName("time")
	val time: String,

	@field:SerializedName("day")
	val day: Int
)

data class Close(

	@field:SerializedName("time")
	val time: String,

	@field:SerializedName("day")
	val day: Int
)

data class PeriodsItem(

	@field:SerializedName("close")
	val close: Close,

	@field:SerializedName("open")
	val open: Open
)

data class AddressComponentsItem(

	@field:SerializedName("types")
	val types: List<String>,

	@field:SerializedName("short_name")
	val shortName: String,

	@field:SerializedName("long_name")
	val longName: String
)

data class ReviewsItem(

	@field:SerializedName("author_name")
	val authorName: String,

	@field:SerializedName("profile_photo_url")
	val profilePhotoUrl: String,

	@field:SerializedName("author_url")
	val authorUrl: String,

	@field:SerializedName("rating")
	val rating: Int,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("text")
	val text: String,

	@field:SerializedName("time")
	val time: Int,

	@field:SerializedName("relative_time_description")
	val relativeTimeDescription: String
)




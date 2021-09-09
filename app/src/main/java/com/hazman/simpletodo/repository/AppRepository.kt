package com.hazman.simpletodo.repository

import com.hazman.simpletodo.model.ToDo
import com.hazman.simpletodo.retrofit.ApiInterface
import com.hazman.simpletodo.retrofit.RetrofitClient


class AppRepository(private val apiInterface: ApiInterface = RetrofitClient.apiInterface) {
    suspend fun getTodoList(page: Int) = apiInterface.getTodoList(page)

    suspend fun setToDo(toDo: ToDo) = apiInterface.setToDo(toDo)

    suspend fun deleteToDo(id: String) = apiInterface.deleteToDo(id)

    suspend fun updateToDo(id: String, toDo: ToDo) = apiInterface.updateToDo(id, toDo)

    suspend fun searchTitle(title: String) = apiInterface.searchTitle(title)

    suspend fun filterCompleted(completed: String) = apiInterface.filterCompleted(completed)

//    suspend fun setLogin(loginParameter: LoginParameter) = apiInterface.setLogin(loginParameter)
//    suspend fun setVerificationCode(verificationCodeParameter: VerificationCodeParameter) =
//        apiInterface.setVerificationCode(verificationCodeParameter)
//
//    suspend fun setResetPassword(resetPasswordParameter: ResetPasswordParameter) =
//        apiInterface.setResetPassword(resetPasswordParameter)
//
//    suspend fun setLogout(logoutParameter: LogoutParameter) =
//        apiInterface.setLogout(logoutParameter)
//
//    suspend fun setUpload(uploadParameter: UploadParameter) =
//        apiInterface.setUpload(uploadParameter)
//
//    suspend fun deleteProfileImage() = apiInterface.deleteProfileImage()
//
//    suspend fun getProfile() = apiInterface.getProfile()
//
//    suspend fun setUpdateProfile(updateProfileParameter: UpdateProfileParameter) =
//        apiInterface.setUpdateProfile(updateProfileParameter)
//
//    suspend fun getTripListUpcoming() = apiInterface.getTripListUpcoming()
//
//    suspend fun getTripListHistory(days: Int?, types: String?, search: String?, page: Int?) =
//        apiInterface.getTripListHistory(days, types, search, page)
//
//    suspend fun getTripDetail(pathId: String) = apiInterface.getTripDetail(pathId)
//
//    suspend fun setTripDepart(departParameter: DepartParameter) =
//        apiInterface.setTripDepart(departParameter)
//
//    suspend fun setTripDeliver(deliverParameter: DeliverParameter) =
//        apiInterface.setTripDeliver(deliverParameter)
//
//    suspend fun setTripReattempt(reattemptParameter: ReattemptParameter) =
//        apiInterface.setTripReattempt(reattemptParameter)
//
//    suspend fun setTripPickup(pickupParameter: PickupParameter) =
//        apiInterface.setTripPickup(pickupParameter)
//
//    suspend fun setTripPickupFail(pickupFailParameter: PickupFailParameter) =
//        apiInterface.setTripPickupFail(pickupFailParameter)
//
//    suspend fun setTripArrive(arriveParameter: ArriveParameter) =
//        apiInterface.setTripArrive(arriveParameter)


}
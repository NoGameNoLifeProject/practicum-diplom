package ru.practicum.android.diploma.data

import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import ru.practicum.android.diploma.data.dto.toDomain
import ru.practicum.android.diploma.data.mapper.MapperSearchVacancyRequestResponse
import ru.practicum.android.diploma.data.mapper.MapperVacancyDetails
import ru.practicum.android.diploma.data.network.dto.GetAreasRequest
import ru.practicum.android.diploma.data.network.dto.GetIndustriesRequest
import ru.practicum.android.diploma.data.network.dto.GetVacancyDetailsRequest
import ru.practicum.android.diploma.data.network.dto.SearchVacanciesRequest
import ru.practicum.android.diploma.domain.api.IStorageRepository
import ru.practicum.android.diploma.domain.api.IVacancyRepository
import ru.practicum.android.diploma.domain.api.Resource
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.ReceivedVacanciesData
import ru.practicum.android.diploma.domain.models.SubIndustry
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.util.NO_INTERNET_ERROR_CODE
import java.net.HttpURLConnection

class VacancyRepositoryImpl(
    private val networkClient: IRetrofitApiClient,
    private val filterParam: IStorageRepository,
    private val searchMapper: MapperSearchVacancyRequestResponse,
    private val vacancyDetailsMapper: MapperVacancyDetails,
    private val networkInfo: NetworkInfoDataSource,
) : IVacancyRepository {

    private var lastRequest: SearchVacanciesRequest? = null
    private var pagesInLastResponse: Int = 0

    override fun searchVacancies(expression: String): Flow<Resource<ReceivedVacanciesData>> = flow {
        val req = searchMapper.mapRequest(expression, filterParam.read())
        lastRequest = req
        val result = networkClient.searchVacancies(req)
        val body = result.body()
        if (result.isSuccessful && body != null) {
            val vacanciesData = searchMapper.mapResponse(body)
            if (vacanciesData.pages != null) {
                pagesInLastResponse = vacanciesData.pages
            }
            emit(Resource.Success(vacanciesData))
        } else {
            emit(Resource.Error(result.code()))
        }
    }.onStart {
        if (!networkInfo.isConnected()) {
            emit(Resource.Error(NO_INTERNET_ERROR_CODE))
            currentCoroutineContext().cancel()
        }
    }

    override fun getAreas(): Flow<Resource<List<Area>>> = flow {
        val result = networkClient.getAreas(GetAreasRequest())
        val body = result.body()
        if (result.isSuccessful && body != null) {
            emit(Resource.Success(body.toList().map { it.toDomain() }))
        } else {
            emit(Resource.Error(result.code()))
        }
    }.onStart {
        if (!networkInfo.isConnected()) {
            emit(Resource.Error(NO_INTERNET_ERROR_CODE))
            currentCoroutineContext().cancel()
        }
    }

    override fun getVacancyDetails(
        vacancyId: String,
    ): Flow<Resource<VacancyDetails>> = flow {
        val result = networkClient.getVacancyDetails(GetVacancyDetailsRequest(vacancyId))
        val body = result.body()
        if (result.isSuccessful && body != null) {
            emit(Resource.Success(vacancyDetailsMapper.map(body)))
        } else {
            emit(Resource.Error(result.code()))
        }
    }.onStart {
        if (!networkInfo.isConnected()) {
            emit(Resource.Error(NO_INTERNET_ERROR_CODE))
            currentCoroutineContext().cancel()
        }
    }

    override fun loadNewVacanciesPage(): Flow<Resource<ReceivedVacanciesData>> = flow {
        val lastReq = lastRequest
        if (lastReq == null) {
            emit(Resource.Error(HttpURLConnection.HTTP_INTERNAL_ERROR))
        } else {
            lastReq.page += 1
//            lastRequest = lastReq
            val result = networkClient.searchVacancies(lastReq)
            val body = result.body()
            if (result.isSuccessful && body != null) {
                val vacanciesData = searchMapper.mapResponse(body)
                if (vacanciesData.pages != null) {
                    pagesInLastResponse = vacanciesData.pages //  мало-ли, вдруг количество изменится
                }
                emit(Resource.Success(vacanciesData))
            } else {
                emit(Resource.Error(result.code()))
            }
        }
    }.onStart {
        if (!networkInfo.isConnected()) {
            emit(Resource.Error(NO_INTERNET_ERROR_CODE))
            currentCoroutineContext().cancel()
        }
    }

    override fun getIndustries(): Flow<Resource<List<Industry>>> = flow {
        val result = networkClient.getIndustries(GetIndustriesRequest())
        val body = result.body()
        if (result.isSuccessful && body != null) {
            emit(
                Resource.Success(
                    body.toList().map { it1 ->
                        Industry(
                            id = it1.id,
                            name = it1.name,
                            subIndustries = it1.subIndustries
                                ?.map { SubIndustry(id = it.id, name = it.name) }
                        )
                    }
                )
            )
        } else {
            emit(Resource.Error(result.code()))
        }
    }.onStart {
        if (!networkInfo.isConnected()) {
            emit(Resource.Error(NO_INTERNET_ERROR_CODE))
            currentCoroutineContext().cancel()
        }
    }
}

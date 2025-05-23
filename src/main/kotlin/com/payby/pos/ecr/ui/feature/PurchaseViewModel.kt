package com.payby.pos.ecr.ui.feature

import com.google.protobuf.Any
import com.payby.pos.ecr.api.Processor
import com.payby.pos.ecr.connect.ConnectionCore
import com.payby.pos.ecr.ui.feature.bean.SaleRequest
import com.payby.pos.ecr.utils.AmountHelper
import com.payby.pos.ecr.utils.Logger
import com.payby.pos.ecr.utils.ThreadPoolManager
import com.uaepay.pos.ecr.Ecr
import com.uaepay.pos.ecr.acquire.Acquire
import com.uaepay.pos.ecr.common.Common

class PurchaseViewModel {
    fun doTransaction (request: SaleRequest) {
        val timestamp = Processor.getTimestamp()
        val money = Common.Money.newBuilder()
            .setAmount(AmountHelper.longCent2String(request.amount))
            .setCurrencyCode("AED")
            .build()

        val listReceipt = ArrayList<Acquire.Receipt>()
        when (request.nReceiptType) {
            1-> {
                listReceipt.add(Acquire.Receipt.CUSTOMER_RECEIPT)
            }
            2-> {
                listReceipt.add(Acquire.Receipt.MERCHANT_RECEIPT)
            }
            3->{
                listReceipt.add(Acquire.Receipt.CUSTOMER_RECEIPT)
                listReceipt.add(Acquire.Receipt.MERCHANT_RECEIPT)
            }
        }

        val listPayment = ArrayList<Acquire.PaymentMethod>()
        when (request.paymentMethod) {
            0 -> {
                Logger.error("<UNK>0")
                listPayment.add(Acquire.PaymentMethod.UNRECOGNIZED)
            }
            1-> {
                Logger.error("<UNK>1")
                listPayment.add(Acquire.PaymentMethod.BANKCARD)
            }
            2-> {
                Logger.error("<UNK>2")
                listPayment.add(Acquire.PaymentMethod.CUSTOMER_PRESENT_CODE)
            }
            3->{
                Logger.error("<UNK>3")
                listPayment.add(Acquire.PaymentMethod.CUSTOMER_PRESENT_CODE)
                listPayment.add(Acquire.PaymentMethod.BANKCARD)
            }
            4->{
                Logger.error("<UNK>4")
                listPayment.add(Acquire.PaymentMethod.POS_PRESENT_CODE)
            }
            5->{
                Logger.error("<UNK>5")
                listPayment.add(Acquire.PaymentMethod.POS_PRESENT_CODE)
                listPayment.add(Acquire.PaymentMethod.BANKCARD)
            }
            6->{
                Logger.error("<UNK>6")
                listPayment.add(Acquire.PaymentMethod.POS_PRESENT_CODE)
                listPayment.add(Acquire.PaymentMethod.CUSTOMER_PRESENT_CODE)
            }
            else -> {
                listPayment.add(Acquire.PaymentMethod.UNRECOGNIZED)
            }
        }

        val cashierParams = Acquire.CashierParams.newBuilder()
            .addAllPrintReceipts(listReceipt)
            .setDisplayResultPage(request.isDisplay)
            .addAllPaymentMethods(listPayment)
            .build()
        val invokeType = when(request.nInvokeType) {
            1-> {
                Acquire.InvokeType.SYNCHRONIZATION
            }
            2-> {
                Acquire.InvokeType.ASYNCHRONIZATION
            }

            else -> {
                Acquire.InvokeType.UNRECOGNIZED
            }
        }
        val notification = when(request.nNotificationType) {
            1-> {
                Acquire.AcquiredResultNotification.REQUEST
            }
            2-> {
                Acquire.AcquiredResultNotification.EVENT
            }
            else -> {
                Acquire.AcquiredResultNotification.UNRECOGNIZED
            }
        }

        val invokeTypeParams = Acquire.InvokeParams.newBuilder()
            .setInvokeType(invokeType)
            .setNotification(notification).build()


        val purchaseRequest = Acquire.PlaceOrderRequest.newBuilder()
            .setAmount(money)
            .setSubject(request.subject)
            .setMerchantOrderNo(request.merchantOrderNo)
            .setInvokeParams(invokeTypeParams)
            .build()


        val body = Any.pack(purchaseRequest)
        val request = Ecr.Request.newBuilder().setMessageId(2).setTimestamp(timestamp).setServiceName(Processor.ACQUIRE_PLACE_ORDER).setBody(body).build()
        val envelope = Ecr.EcrEnvelope.newBuilder().setVersion(Processor.VERSION).setRequest(request).build()
        Processor.printRequest(envelope)
        val byteArray = envelope.toByteArray()
        ThreadPoolManager.executeCacheTask { ConnectionCore.send(byteArray) }

    }
}
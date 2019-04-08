package com.meesho.price;

import dbutils.MysqlCon;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import restassured.RestUtil;
import utility.Constants;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import utility.PriceInsertClient;

/**
 * Created by lokeshmadivala on 03/04/19.
 */
public class InsertPostAPITest{
    Connection con;
    private Response response;
    static Random rand = new Random();
    static int supplier_id = rand.nextInt(10000);
    static int  product_id = rand.nextInt(1000);
    static int variation_id = rand.nextInt(100);
    static int supplier_id1 = rand.nextInt(1000);
    static int product_id1 = rand.nextInt(10000);
    static int variation_id1 = rand.nextInt(100000);
    static int supplier_id2 = rand.nextInt(1000000000);
    static int product_id2 = rand.nextInt(1000000000);
    static int variation_id2 = rand.nextInt(1000000000);
    static int price = rand.nextInt(1000000000);
    static int mrp_price = rand.nextInt(1000000000);
    int supplier_id3 = rand.nextInt(10000);
    int product_id3 = rand.nextInt(1000);
    int variation_id3 = rand.nextInt(100);
    int price1 = rand.nextInt(1000);
    int mrp_price1 = rand.nextInt(100);
    int original_price=rand.nextInt(100000);
    int supplier_cost=rand.nextInt(100000);
    int input_price=rand.nextInt(100000);
    int input_price_inc_gst=rand.nextInt(10);
    int shipping_charges_adjustment_amount=rand.nextInt(100);
    int shipping_charges=rand.nextInt(100);
    int monetization_type=rand.nextInt(100);
    int monetization_percent=rand.nextInt(100);
    int gst_rate=rand.nextInt(10);

    private static Map<String, String> headers = new HashMap<String, String>();
    MysqlCon mysqlCon = new MysqlCon();

    @BeforeClass
    public void setUp() throws Exception {
        headers.put("Authentication", "test123");
        headers.put("Content-Type", "application/json");
        headers.put("Postman-Token", "4a01b704-1d51-4598-a57d-654be799bef7,58db56e1-b02a-4951-8b9d-9687080ec8b5");
        headers.put("cache-control", "no-cache,no-cache");
        con = mysqlCon.DatabaseConnector("meesho-supply-v2-db-test-2.cjdqi0zr8ovs.ap-southeast-1.rds.amazonaws.com", "price", "mycroft", "poirot20");

    }
    @Test(enabled = true, description = "Validate Price Insert Post call without header", priority = 0)
    public void InsertPricePostCallWithoutHeader()
    {
        headers.remove("Authentication");
        String payload = PriceInsertClient.createPriceInsertPayload(supplier_id, product_id, variation_id, 800, 768,
                900, 88, 884, 6, 30,
                70, 5, 7, 3);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers,payload,403);
        PriceInsertClient.verifyCallbackError(response, "Forbidden","Access Denied", "/v1/product-price/insert");
        headers.put("Authentication","test123");
    }
    @Test(enabled = true, description = "Validate Price Insert Post call with wrong header", priority = 1)
    public void InsertPricePostCallWithWrongHeader()
    {
        headers.put("Authentication","test1234");
        String payload = PriceInsertClient.createPriceInsertPayload(supplier_id, product_id, variation_id, 800, 768,
                900, 88, 884, 6, 30,
                70, 5, 7, 3);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers,payload,403);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers,payload,403);
        PriceInsertClient.verifyCallbackError(response, "Forbidden","Access Denied","/v1/product-price/insert");
        headers.put("Authentication","test123");
    }
    @Test(enabled = true, description = "Validate Price Inesrt post call with single data", priority = 2)
    public void PriceInsertSinglePostCall() throws Exception {
        String payload = PriceInsertClient.createPriceInsertPayload(supplier_id, product_id, variation_id, 800, 768,
                900, 88, 884, 6, 30,
                70, 5, 7, 3);
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("supplier_id", supplier_id);
        map.put("product_id", product_id);
        map.put("variation_id", variation_id);
        map.put("price", 800);
        map.put("mrp_price", 768);
        map.put("original_price", 900);
        map.put("supplier_cost", 88);
        map.put("input_price", 884);
        map.put("input_price_inc_gst", 6);
        map.put("shipping_charges_adjustment_amount", 30);
        map.put("shipping_charges", 70);
        map.put("monetization_type", 5);
        map.put("monetization_percent", 7);
        map.put("gst_rate", 3);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 200);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().get("service_message"), "Price data inserted successfully for given product, supplier and variation ids");
        Assert.assertEquals(response.jsonPath().get("service_code"), "0001");
        Assert.assertEquals(response.jsonPath().get("number_of_rows_inserted"), 1);

        ArrayList<String> data = mysqlCon.readDatabaseRecords(con, "Select * from product_supplier_price_map where supplier_id='" + supplier_id + "' and product_id='" + product_id + "' and '" + variation_id + "'");

        int length=PriceInsertClient.Validate(map, data);
        Assert.assertEquals(length, 14);


    }


    @Test(enabled = true, description = "Validate Price Inesrt post call with Max Length Payloada", priority = 3)
    public void PriceInsertMax_LengthPostCall() throws Exception {
        String payload = PriceInsertClient.createMax_LengthInsertPayload(supplier_id2, product_id2, variation_id2, price, mrp_price,
                686781098, 145783487, 785478458, 6, 478773824,
                237867777, 237884834, 934884834, 893884834);
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("supplier_id", supplier_id2);
        map.put("product_id", product_id2);
        map.put("variation_id", variation_id2);
        map.put("price", price);
        map.put("mrp_price", mrp_price);
        map.put("original_price", 686781098);
        map.put("supplier_cost", 145783487);
        map.put("input_price", 785478458);
        map.put("input_price_inc_gst", 6);
        map.put("shipping_charges_adjustment_amount", 478773824);
        map.put("shipping_charges", 237867777);
        map.put("monetization_type", 237884834);
        map.put("monetization_percent", 934884834);
        map.put("gst_rate", 893884834);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 200);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().get("service_message"), "Price data inserted successfully for given product, supplier and variation ids");
        Assert.assertEquals(response.jsonPath().get("service_code"), "0001");
        Assert.assertEquals(response.jsonPath().get("number_of_rows_inserted"), 1);
        MysqlCon mysqlCon = new MysqlCon();
        Connection con = mysqlCon.DatabaseConnector("meesho-supply-v2-db-test-2.cjdqi0zr8ovs.ap-southeast-1.rds.amazonaws.com", "price", "mycroft", "poirot20");
        ArrayList<String> data = mysqlCon.readDatabaseRecords(con, "Select * from product_supplier_price_map where supplier_id='" + supplier_id2 + "' and product_id='" + product_id2 + "' and '" + variation_id2 + "'");

        int length=PriceInsertClient.Validate(map, data);
        Assert.assertEquals(length, 14);


    }

    @Test(enabled = true, description = "Validate Price Insert post call with multiple data", priority = 4)
    public void PriceInsertMultiplePostCall() throws Exception {
        String payload = PriceInsertClient.createPriceInsertMultiPayload(supplier_id, product_id, variation_id, 800, 768,
                900, 88, 884, 6, 30,
                70, 5, 7, 3, supplier_id1, product_id1, variation_id1, price, mrp_price,
                original_price, supplier_cost, input_price, input_price_inc_gst, shipping_charges_adjustment_amount,
                shipping_charges, monetization_type, monetization_percent, gst_rate);
        System.out.println(payload);
        HashMap<String, Integer> map1 = new HashMap<String, Integer>();
        map1.put("supplier_id", supplier_id);
        map1.put("product_id", product_id);
        map1.put("variation_id", variation_id);
        map1.put("price", 800);
        map1.put("mrp_price", 768);
        map1.put("original_price", 900);
        map1.put("supplier_cost", 88);
        map1.put("input_price", 884);
        map1.put("input_price_inc_gst", 6);
        map1.put("shipping_charges_adjustment_amount", 30);
        map1.put("shipping_charges", 70);
        map1.put("monetization_type", 5);
        map1.put("monetization_percent", 7);
        map1.put("gst_rate", 3);

        HashMap<String, Integer> map2 = new HashMap<String, Integer>();
        map2.put("supplier_id", supplier_id1);
        map2.put("product_id", product_id1);
        map2.put("variation_id", variation_id1);
        map2.put("price", price);
        map2.put("mrp_price", mrp_price);
        map2.put("original_price", original_price);
        map2.put("supplier_cost", supplier_cost);
        map2.put("input_price", input_price);
        map2.put("input_price_inc_gst", input_price_inc_gst);
        map2.put("shipping_charges_adjustment_amount", shipping_charges_adjustment_amount);
        map2.put("shipping_charges", shipping_charges);
        map2.put("monetization_type", monetization_type);
        map2.put("monetization_percent", monetization_percent);
        map2.put("gst_rate", gst_rate);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 200);
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().get("service_message"), "Price data inserted successfully for given product, supplier and variation ids");
        Assert.assertEquals(response.jsonPath().get("service_code"), "0001");
        Assert.assertEquals(response.jsonPath().get("number_of_rows_inserted"), 2);
        MysqlCon mysqlCon = new MysqlCon();
        Connection con = mysqlCon.DatabaseConnector("meesho-supply-v2-db-test-2.cjdqi0zr8ovs.ap-southeast-1.rds.amazonaws.com", "price", "mycroft", "poirot20");
        ArrayList<String> data = mysqlCon.readDatabaseRecords(con, "Select * from product_supplier_price_map where supplier_id='" + supplier_id + "' and product_id='" + product_id + "' and '" + variation_id + "'");
        ArrayList<String> data1 = mysqlCon.readDatabaseRecords(con, "Select * from product_supplier_price_map where supplier_id='" + supplier_id1 + "' and product_id='" + product_id1 + "' and '" + variation_id1 + "'");
        int length=PriceInsertClient.Validate(map1, data);
        Assert.assertEquals(length, 14);
        int length1=PriceInsertClient.Validate(map2, data1);;
        Assert.assertEquals(length1, 14);
    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with Mandatory data", priority = 5)
    public void PriceInsertMandatoryPostCall() throws Exception {
        String payload = PriceInsertClient.createMandatoryParameterInsertPayload(supplier_id3, product_id3, variation_id3, price1, mrp_price1);
        System.out.println(payload);
        HashMap<String, Integer> map3 = new HashMap<String, Integer>();
        map3.put("supplier_id", supplier_id3);
        map3.put("product_id", product_id3);
        map3.put("variation_id", variation_id3);
        map3.put("price", price1);
        map3.put("mrp_price", mrp_price1);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 200);
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().get("service_message"), "Price data inserted successfully for given product, supplier and variation ids");
        Assert.assertEquals(response.jsonPath().get("service_code"), "0001");
        Assert.assertEquals(response.jsonPath().get("number_of_rows_inserted"), 1);
        MysqlCon mysqlCon = new MysqlCon();
        Connection con = mysqlCon.DatabaseConnector("meesho-supply-v2-db-test-2.cjdqi0zr8ovs.ap-southeast-1.rds.amazonaws.com", "price", "mycroft", "poirot20");
        ArrayList<String> data1 = mysqlCon.readDatabaseRecords(con, "Select * from product_supplier_price_map where supplier_id='" + supplier_id3 + "' and product_id='" + product_id3 + "' and '" + variation_id3 + "'");
        int length = PriceInsertClient.validateValues(map3, data1);
        Assert.assertEquals(length, 5);
    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with NULL Supplier_ID", priority = 6)
    public void PriceInsertNULLSupplier_IDPostCall() throws Exception {
        String payload = PriceInsertClient.createNULLSupplier_IDInsertPayload(null, product_id, variation_id, 800, 768,
                900, 88, 884, 6, 30,
                70, 5, 7, 3);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        response.prettyPrint();
        String resp = response.toString();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        resp.compareTo("Key parameters are missing");

    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with NULL Product_ID", priority = 7)
    public void PriceInsertNULLProduct_IdPostCall() throws Exception {
        String payload = PriceInsertClient.createNULLProduct_IDInsertPayload(supplier_id, null, variation_id, 800, 768,
                900, 88, 884, 6, 30,
                70, 5, 7, 3);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        String resp = response.toString();
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        resp.compareTo("Key parameters are missing");

    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with NULL Variation_ID", priority = 8)
    public void PriceInsertNULLVariation_IdPostCall() throws Exception {
        String payload = PriceInsertClient.createNULLVariation_IDInsertPayload(supplier_id, product_id, null, 800, 768,
                900, 88, 884, 6, 30,
                70, 5, 7, 3);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        String resp = response.toString();
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        resp.compareTo("Key parameters are missing");

    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with NULL Price", priority = 9)
    public void PriceInsertNULLPricePostCall() throws Exception {
        String payload = PriceInsertClient.createNULLPriceInsertPayload(supplier_id, product_id, variation_id, null, 768,
                900, 88, 884, 6, 30,
                70, 5, 7, 3);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        String resp = response.toString();
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        resp.compareTo("Key parameters are missing");

    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with NULL Mrp_price", priority = 10)
    public void PriceInsertNULLMrp_PricePostCall() throws Exception {
        String payload = PriceInsertClient.createNULLMrp_PricePriceInsertPayload(supplier_id, product_id, variation_id, 500, null,
                900, 88, 884, 6, 30,
                70, 5, 7, 3);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        String resp = response.toString();
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        resp.compareTo("Key parameters are missing");
    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with String Supplier_ID", priority = 11)
    public void PriceInsertStringSupplier_IdPostCall() throws Exception {
        String payload = PriceInsertClient.createStringSupplier_IDPriceInsertPayload("yufuy", product_id, variation_id, 500, 400,
                900, 88, 884, 6, 30,
                70, 5, 7, 3);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("error"), "Bad Request");
        Assert.assertEquals(response.jsonPath().get("message"), "JSON parse error: Unrecognized token 'yufuy': was expecting ('true', 'false' or 'null')\n" +
                " at [Source: (PushbackInputStream); line: 2, column: 22]; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Unrecognized token 'yufuy': was expecting ('true', 'false' or 'null')\n" +
                " at [Source: (PushbackInputStream); line: 2, column: 22]\n" +
                " at [Source: (PushbackInputStream); line: 2, column: 2] (through reference chain: com.meesho.pricing.data.request.ProductSupplierPriceMapRequest[\"data\"]->java.util.ArrayList[0])");
    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with String Product ID", priority = 12)
    public void PriceInsertStringProduct_IdPostCall() throws Exception {
        String payload = PriceInsertClient.createStringProduct_IDPriceInsertPayload(supplier_id, "CFGYYU", variation_id, 500, 400,
                900, 88, 884, 6, 30,
                70, 5, 7, 3);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("error"), "Bad Request");
        Assert.assertEquals(response.jsonPath().get("message"), "JSON parse error: Unrecognized token 'CFGYYU': was expecting ('true', 'false' or 'null')\n" +
                " at [Source: (PushbackInputStream); line: 3, column: 26]; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Unrecognized token 'CFGYYU': was expecting ('true', 'false' or 'null')\n" +
                " at [Source: (PushbackInputStream); line: 3, column: 26]\n" +
                " at [Source: (PushbackInputStream); line: 3, column: 5] (through reference chain: com.meesho.pricing.data.request.ProductSupplierPriceMapRequest[\"data\"]->java.util.ArrayList[0])");

    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with String Variation_ID", priority = 13)
    public void PriceInsertStringVariation_IdPostCall() throws Exception {
        String payload = PriceInsertClient.createStringVariation_IDPriceInsertPayload(supplier_id, product_id, "GHHnn", 500, 400,
                900, 88, 884, 6, 30,
                70, 5, 7, 3);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("error"), "Bad Request");
        Assert.assertEquals(response.jsonPath().get("message"), "JSON parse error: Unrecognized token 'GHHnn': was expecting ('true', 'false' or 'null')\n at [Source: (PushbackInputStream); line: 4, column: 27]; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Unrecognized token 'GHHnn': was expecting ('true', 'false' or 'null')\n at [Source: (PushbackInputStream); line: 4, column: 27]\n at [Source: (PushbackInputStream); line: 4, column: 5] (through reference chain: com.meesho.pricing.data.request.ProductSupplierPriceMapRequest[\"data\"]->java.util.ArrayList[0])");
    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with String Price", priority = 14)
    public void PriceInsertStringPricePostCall() throws Exception {
        String payload = PriceInsertClient.createStringPricePriceInsertPayload(supplier_id, product_id, variation_id, "10#$#$SD", 400,
                900, 88, 884, 6, 30,
                70, 5, 7, 3);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("error"), "Bad Request");
        Assert.assertEquals(response.jsonPath().get("message"), "JSON parse error: Unexpected character ('#' (code 35)): was expecting comma to separate Object entries\n" +
                " at [Source: (PushbackInputStream); line: 5, column: 17]; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Unexpected character ('#' (code 35)): was expecting comma to separate Object entries\n" +
                " at [Source: (PushbackInputStream); line: 5, column: 17]\n" +
                " at [Source: (PushbackInputStream); line: 5, column: 14] (through reference chain: com.meesho.pricing.data.request.ProductSupplierPriceMapRequest[\"data\"]->java.util.ArrayList[0])");
    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with String Mrp_Price", priority = 15)
    public void PriceInsertStringMrp_PricePostCall() throws Exception {
        String payload = PriceInsertClient.createStringMrp_PricePriceInsertPayload(supplier_id, product_id, variation_id, 100, "345&$DFF",
                900, 88, 884, 6, 30,
                70, 5, 7, 3);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("error"), "Bad Request");
        Assert.assertEquals(response.jsonPath().get("message"), "JSON parse error: Unexpected character ('&' (code 38)): was expecting comma to separate Object entries\n" +
                " at [Source: (PushbackInputStream); line: 6, column: 22]; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Unexpected character ('&' (code 38)): was expecting comma to separate Object entries\n" +
                " at [Source: (PushbackInputStream); line: 6, column: 22]\n" +
                " at [Source: (PushbackInputStream); line: 6, column: 18] (through reference chain: com.meesho.pricing.data.request.ProductSupplierPriceMapRequest[\"data\"]->java.util.ArrayList[0])");


    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with String Original_Price", priority = 16)
    public void PriceInsertStringOriginal_PricePricePostCall() throws Exception {
        String payload = PriceInsertClient.createStringOriginal_PricePriceInsertPayload(supplier_id, product_id, variation_id, 100, 100,
                "GYUY", 100, 300, 400, 366,
                70, 12, 3, 1);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("error"), "Bad Request");
        Assert.assertEquals(response.jsonPath().get("message"), "JSON parse error: Unrecognized token 'GYUY': was expecting ('true', 'false' or 'null')\n" +
                " at [Source: (PushbackInputStream); line: 7, column: 27]; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Unrecognized token 'GYUY': was expecting ('true', 'false' or 'null')\n" +
                " at [Source: (PushbackInputStream); line: 7, column: 27]\n" +
                " at [Source: (PushbackInputStream); line: 7, column: 5] (through reference chain: com.meesho.pricing.data.request.ProductSupplierPriceMapRequest[\"data\"]->java.util.ArrayList[0])");


    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with String Supplier_Cost", priority = 17)
    public void PriceInsertStringSupplierCostPostCall() throws Exception {
        String payload = PriceInsertClient.createStringSupplier_CostPriceInsertPayload(supplier_id, product_id, variation_id, 100, 100,
                200, "ywyuuu#", 300, 400, 366,
                70, 12, 3, 1);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("error"), "Bad Request");
        Assert.assertEquals(response.jsonPath().get("message"), "JSON parse error: Unrecognized token 'ywyuuu': was expecting ('true', 'false' or 'null')\n" +
                " at [Source: (PushbackInputStream); line: 8, column: 28]; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Unrecognized token 'ywyuuu': was expecting ('true', 'false' or 'null')\n" +
                " at [Source: (PushbackInputStream); line: 8, column: 28]\n" +
                " at [Source: (PushbackInputStream); line: 8, column: 5] (through reference chain: com.meesho.pricing.data.request.ProductSupplierPriceMapRequest[\"data\"]->java.util.ArrayList[0])");


    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with String input_price", priority = 18)
    public void PriceInsertStringInput_PricePricePostCall() throws Exception {
        String payload = PriceInsertClient.createStringInput_PricePriceInsertPayload(supplier_id, product_id, variation_id, 100, 100,
                200, 300, "hfueu", 400, 366,
                70, 12, 3, 1);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("error"), "Bad Request");
        Assert.assertEquals(response.jsonPath().get("message"), "JSON parse error: Unrecognized token 'hfueu': was expecting ('true', 'false' or 'null')\n" +
                " at [Source: (PushbackInputStream); line: 9, column: 26]; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Unrecognized token 'hfueu': was expecting ('true', 'false' or 'null')\n" +
                " at [Source: (PushbackInputStream); line: 9, column: 26]\n" +
                " at [Source: (PushbackInputStream); line: 9, column: 5] (through reference chain: com.meesho.pricing.data.request.ProductSupplierPriceMapRequest[\"data\"]->java.util.ArrayList[0])");


    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with String input_price_inc_gst", priority = 19)
    public void PriceInsertStringInput_Price_Inc_GstPostCall() throws Exception {
        String payload = PriceInsertClient.createStringinput_price_inc_gstPriceInsertPayload(supplier_id, product_id, variation_id, 100, 100,
                200, 300, 500, "hsdh", 366,
                70, 12, 3, 1);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("error"), "Bad Request");
        Assert.assertEquals(response.jsonPath().get("message"), "JSON parse error: Unrecognized token 'hsdh': was expecting ('true', 'false' or 'null')\n" +
                " at [Source: (PushbackInputStream); line: 10, column: 33]; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Unrecognized token 'hsdh': was expecting ('true', 'false' or 'null')\n" +
                " at [Source: (PushbackInputStream); line: 10, column: 33]\n" +
                " at [Source: (PushbackInputStream); line: 10, column: 5] (through reference chain: com.meesho.pricing.data.request.ProductSupplierPriceMapRequest[\"data\"]->java.util.ArrayList[0])");


    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with String shipping_charges_adjustment_amount", priority = 20)
    public void PriceInsertStringShipping_Charges_Adjustment_AmountPricePostCall() throws Exception {
        String payload = PriceInsertClient.createStringshipping_charges_adjustment_amountPriceInsertPayload(supplier_id, product_id, variation_id, 100, 100,
                200, 300, 500, 300, "hyeuyau",
                70, 12, 3, 1);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("error"), "Bad Request");
        Assert.assertEquals(response.jsonPath().get("message"), "JSON parse error: Unrecognized token 'hyeuyau': was expecting ('true', 'false' or 'null')\n" +
                " at [Source: (PushbackInputStream); line: 11, column: 50]; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Unrecognized token 'hyeuyau': was expecting ('true', 'false' or 'null')\n" +
                " at [Source: (PushbackInputStream); line: 11, column: 50]\n" +
                " at [Source: (PushbackInputStream); line: 11, column: 5] (through reference chain: com.meesho.pricing.data.request.ProductSupplierPriceMapRequest[\"data\"]->java.util.ArrayList[0])");


    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with String shipping_charges", priority = 21)
    public void PriceInsertStringShipping_ChargesPricePostCall() throws Exception {
        String payload = PriceInsertClient.createStringshipping_chargesPriceInsertPayload(supplier_id, product_id, variation_id, 100, 100,
                200, 300, 500, 300, 70,
                "gsduy", 12, 3, 1);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("error"), "Bad Request");
        Assert.assertEquals(response.jsonPath().get("message"), "JSON parse error: Unrecognized token 'gsduy': was expecting ('true', 'false' or 'null')\n" +
                " at [Source: (PushbackInputStream); line: 12, column: 31]; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Unrecognized token 'gsduy': was expecting ('true', 'false' or 'null')\n" +
                " at [Source: (PushbackInputStream); line: 12, column: 31]\n" +
                " at [Source: (PushbackInputStream); line: 12, column: 5] (through reference chain: com.meesho.pricing.data.request.ProductSupplierPriceMapRequest[\"data\"]->java.util.ArrayList[0])");


    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with String monetization_type", priority = 22)
    public void PriceInsertStringMonetization_TypePricePostCall() throws Exception {
        String payload = PriceInsertClient.createStringmonetization_typePriceInsertPayload(supplier_id, product_id, variation_id, 100, 100,
                200, 300, 500, 300, 70,
                4000, "hudsuh", 3, 1);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("error"), "Bad Request");
        Assert.assertEquals(response.jsonPath().get("message"), "JSON parse error: Unrecognized token 'hudsuh': was expecting ('true', 'false' or 'null')\n" +
                " at [Source: (PushbackInputStream); line: 13, column: 32]; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Unrecognized token 'hudsuh': was expecting ('true', 'false' or 'null')\n" +
                " at [Source: (PushbackInputStream); line: 13, column: 32]\n" +
                " at [Source: (PushbackInputStream); line: 13, column: 5] (through reference chain: com.meesho.pricing.data.request.ProductSupplierPriceMapRequest[\"data\"]->java.util.ArrayList[0])");


    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with String monetization_percent", priority = 23)
    public void PriceInsertStringMonetization_PercentPricePostCall() throws Exception {
        String payload = PriceInsertClient.createStringmonetization_percentPriceInsertPayload(supplier_id, product_id, variation_id, 100, 100,
                200, 300, 500, 300, 70,
                4000, 10, "he", 1);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("error"), "Bad Request");
        Assert.assertEquals(response.jsonPath().get("message"), "JSON parse error: Unrecognized token 'he': was expecting ('true', 'false' or 'null')\n" +
                " at [Source: (PushbackInputStream); line: 14, column: 31]; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Unrecognized token 'he': was expecting ('true', 'false' or 'null')\n" +
                " at [Source: (PushbackInputStream); line: 14, column: 31]\n" +
                " at [Source: (PushbackInputStream); line: 14, column: 5] (through reference chain: com.meesho.pricing.data.request.ProductSupplierPriceMapRequest[\"data\"]->java.util.ArrayList[0])");


    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with String gst_rate", priority = 24)
    public void PriceInsertStringGst_RatePricePostCall() throws Exception {
        String payload = PriceInsertClient.createStringgst_ratePriceInsertPayload(supplier_id, product_id, variation_id, 100, 100,
                200, 300, 500, 300, 70,
                4000, 10, 5, "S");
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("error"), "Bad Request");
        Assert.assertEquals(response.jsonPath().get("message"), "JSON parse error: Unrecognized token 'S': was expecting ('true', 'false' or 'null')\n" +
                " at [Source: (PushbackInputStream); line: 15, column: 23]; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Unrecognized token 'S': was expecting ('true', 'false' or 'null')\n" +
                " at [Source: (PushbackInputStream); line: 15, column: 23]\n" +
                " at [Source: (PushbackInputStream); line: 15, column: 10] (through reference chain: com.meesho.pricing.data.request.ProductSupplierPriceMapRequest[\"data\"]->java.util.ArrayList[0])");
    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with Max SupplierID", priority = 25)
    public void PriceInsertMax_SupplierIDPricePostCall() throws Exception {
        String payload = PriceInsertClient.createMax_SupplierIDPriceInsertPayload("2676746776", product_id, variation_id, price, mrp_price,
                200, 300, 500, 300, 70,
                4000, 10, 5, 3);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("error"), "Bad Request");
        Assert.assertEquals(response.jsonPath().get("message"), "JSON parse error: Numeric value (2676746776) out of range of int\n" +
                " at [Source: (PushbackInputStream); line: 2, column: 26]; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Numeric value (2676746776) out of range of int\n" +
                " at [Source: (PushbackInputStream); line: 2, column: 26]\n" +
                " at [Source: (PushbackInputStream); line: 2, column: 16] (through reference chain: com.meesho.pricing.data.request.ProductSupplierPriceMapRequest[\"data\"]->java.util.ArrayList[0]->com.meesho.pricing.data.request.ProductSupplierPriceMapObject[\"supplier_id\"])");
    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with Max ProductID", priority = 26)
    public void PriceInsertMax_ProductIDPricePostCall() throws Exception {
        String payload = PriceInsertClient.createMax_ProductIDPriceInsertPayload(supplier_id, "0987623234", variation_id, price, mrp_price,
                200, 300, 500, 300, 70,
                4000, 10, 5, 3);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("error"), "Bad Request");
        Assert.assertEquals(response.jsonPath().get("message"), "JSON parse error: Invalid numeric value: Leading zeroes not allowed\n" +
                " at [Source: (PushbackInputStream); line: 3, column: 20]; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Invalid numeric value: Leading zeroes not allowed\n" +
                " at [Source: (PushbackInputStream); line: 3, column: 20]\n" +
                " at [Source: (PushbackInputStream); line: 3, column: 5] (through reference chain: com.meesho.pricing.data.request.ProductSupplierPriceMapRequest[\"data\"]->java.util.ArrayList[0])");
    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with Max VariationID", priority = 27)
    public void PriceInsertMax_VariationIDPricePostCall() throws Exception {
        String payload = PriceInsertClient.createMax_VariationIDPriceInsertPayload(supplier_id, product_id, "4787738245", price, mrp_price,
                200, 300, 500, 300, 70,
                4000, 10, 5, 3);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("error"), "Bad Request");
        Assert.assertEquals(response.jsonPath().get("message"), "JSON parse error: Numeric value (4787738245) out of range of int\n" +
                " at [Source: (PushbackInputStream); line: 4, column: 31]; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Numeric value (4787738245) out of range of int\n" +
                " at [Source: (PushbackInputStream); line: 4, column: 31]\n" +
                " at [Source: (PushbackInputStream); line: 4, column: 21] (through reference chain: com.meesho.pricing.data.request.ProductSupplierPriceMapRequest[\"data\"]->java.util.ArrayList[0]->com.meesho.pricing.data.request.ProductSupplierPriceMapObject[\"variation_id\"])");
    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with Max Price", priority = 28)
    public void PriceInsertMax_PricePricePostCall() throws Exception {
        String payload = PriceInsertClient.createMax_PricePriceInsertPayload(supplier_id, product_id, variation_id, "6236264367", mrp_price,
                200, 300, 500, 300, 70,
                4000, 10, 5, 3);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("error"), "Bad Request");
        Assert.assertEquals(response.jsonPath().get("message"), "JSON parse error: Numeric value (6236264367) out of range of int\n" +
                " at [Source: (PushbackInputStream); line: 5, column: 24]; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Numeric value (6236264367) out of range of int\n" +
                " at [Source: (PushbackInputStream); line: 5, column: 24]\n" +
                " at [Source: (PushbackInputStream); line: 5, column: 14] (through reference chain: com.meesho.pricing.data.request.ProductSupplierPriceMapRequest[\"data\"]->java.util.ArrayList[0]->com.meesho.pricing.data.request.ProductSupplierPriceMapObject[\"price\"])");
    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with Max Mrp_Price", priority = 29)
    public void PriceInsertMax_MrpPricePricePostCall() throws Exception {
        String payload = PriceInsertClient.createMax_Mrp_PricePriceInsertPayload(supplier_id, product_id, variation_id, price, "62367236723",
                200, 300, 500, 300, 70,
                4000, 10, 5, 3);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("error"), "Bad Request");
        Assert.assertEquals(response.jsonPath().get("message"), "JSON parse error: Numeric value (62367236723) out of range of int\n" +
                " at [Source: (PushbackInputStream); line: 6, column: 29]; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Numeric value (62367236723) out of range of int\n" +
                " at [Source: (PushbackInputStream); line: 6, column: 29]\n" +
                " at [Source: (PushbackInputStream); line: 6, column: 18] (through reference chain: com.meesho.pricing.data.request.ProductSupplierPriceMapRequest[\"data\"]->java.util.ArrayList[0]->com.meesho.pricing.data.request.ProductSupplierPriceMapObject[\"mrp_price\"])");
    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with Max OriginalPrice", priority = 30)
    public void PriceInsertMax_OriginalPricePricePostCall() throws Exception {
        String payload = PriceInsertClient.createMax_OriginalPricePriceInsertPayload(supplier_id, product_id, variation_id, price, mrp_price,
                "672367623723", 300, 500, 300, 70,
                4000, 10, 5, 3);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("error"), "Bad Request");
        Assert.assertEquals(response.jsonPath().get("message"), "JSON parse error: Numeric value (672367623723) out of range of int\n" +
                " at [Source: (PushbackInputStream); line: 7, column: 34]; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Numeric value (672367623723) out of range of int\n" +
                " at [Source: (PushbackInputStream); line: 7, column: 34]\n" +
                " at [Source: (PushbackInputStream); line: 7, column: 22] (through reference chain: com.meesho.pricing.data.request.ProductSupplierPriceMapRequest[\"data\"]->java.util.ArrayList[0]->com.meesho.pricing.data.request.ProductSupplierPriceMapObject[\"original_price\"])");
    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with Max SupplierCost", priority = 31)
    public void PriceInsertMax_SupplierCostPricePostCall() throws Exception {
        String payload = PriceInsertClient.createMax_SupplierCostPriceInsertPayload(supplier_id, product_id, variation_id, price, mrp_price,
                500, "762367672366", 500, 300, 70,
                4000, 10, 5, 3);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("error"), "Bad Request");
        Assert.assertEquals(response.jsonPath().get("message"), "JSON parse error: Numeric value (762367672366) out of range of int\n" +
                " at [Source: (PushbackInputStream); line: 8, column: 33]; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Numeric value (762367672366) out of range of int\n" +
                " at [Source: (PushbackInputStream); line: 8, column: 33]\n" +
                " at [Source: (PushbackInputStream); line: 8, column: 21] (through reference chain: com.meesho.pricing.data.request.ProductSupplierPriceMapRequest[\"data\"]->java.util.ArrayList[0]->com.meesho.pricing.data.request.ProductSupplierPriceMapObject[\"supplier_cost\"])");
    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with Max Input Price", priority = 32)
    public void PriceInsertMax_InputPricePricePostCall() throws Exception {
        String payload = PriceInsertClient.createMax_InputPricePriceInsertPayload(supplier_id, product_id, variation_id, price, mrp_price,
                500, 600, "6327823785", 300, 70,
                4000, 10, 5, 3);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("error"), "Bad Request");
        Assert.assertEquals(response.jsonPath().get("message"), "JSON parse error: Numeric value (6327823785) out of range of int\n" +
                " at [Source: (PushbackInputStream); line: 9, column: 30]; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Numeric value (6327823785) out of range of int\n" +
                " at [Source: (PushbackInputStream); line: 9, column: 30]\n" +
                " at [Source: (PushbackInputStream); line: 9, column: 20] (through reference chain: com.meesho.pricing.data.request.ProductSupplierPriceMapRequest[\"data\"]->java.util.ArrayList[0]->com.meesho.pricing.data.request.ProductSupplierPriceMapObject[\"input_price\"])");
    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with Max Input Price Inc GST", priority = 33)
    public void PriceInsertMax_InputPriceInc_GSTPricePostCall() throws Exception {
        String payload = PriceInsertClient.createMax_InputPriceInc_GSTPriceInsertPayload(supplier_id, product_id, variation_id, price, mrp_price,
                500, 600, 7000, "874334347327", 70,
                4000, 10, 5, 3);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("error"), "Bad Request");
        Assert.assertEquals(response.jsonPath().get("message"), "JSON parse error: Numeric value (874334347327) out of range of int\n" +
                " at [Source: (PushbackInputStream); line: 10, column: 40]; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Numeric value (874334347327) out of range of int\n" +
                " at [Source: (PushbackInputStream); line: 10, column: 40]\n" +
                " at [Source: (PushbackInputStream); line: 10, column: 28] (through reference chain: com.meesho.pricing.data.request.ProductSupplierPriceMapRequest[\"data\"]->java.util.ArrayList[0]->com.meesho.pricing.data.request.ProductSupplierPriceMapObject[\"input_price_inc_gst\"])");
    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with Max Shipping_Charges_Adjustment_Amount", priority = 34)
    public void PriceInsertMax_Shipping_Charges_Adjustment_AmountPricePostCall() throws Exception {
        String payload = PriceInsertClient.createMax_Shipping_Charges_Adjustment_AmountPriceInsertPayload(supplier_id, product_id, variation_id, price, mrp_price,
                500, 600, 7000, 300, "236777237823",
                4000, 10, 5, 3);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("error"), "Bad Request");
        Assert.assertEquals(response.jsonPath().get("message"), "JSON parse error: Numeric value (236777237823) out of range of int\n" +
                " at [Source: (PushbackInputStream); line: 11, column: 54]; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Numeric value (236777237823) out of range of int\n" +
                " at [Source: (PushbackInputStream); line: 11, column: 54]\n" +
                " at [Source: (PushbackInputStream); line: 11, column: 42] (through reference chain: com.meesho.pricing.data.request.ProductSupplierPriceMapRequest[\"data\"]->java.util.ArrayList[0]->com.meesho.pricing.data.request.ProductSupplierPriceMapObject[\"shipping_charges_adjustment_amount\"])");
    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with Max Shipping_Charges_Adjustment_Amount", priority = 35)
    public void PriceInsertMax_Shipping_ChargesPricePostCall() throws Exception {
        String payload = PriceInsertClient.createMax_Shipping_ChargesPriceInsertPayload(supplier_id, product_id, variation_id, price, mrp_price,
                500, 600, 7000, 300, 400,
                "3627723672367", 10, 5, 3);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("error"), "Bad Request");
        Assert.assertEquals(response.jsonPath().get("message"), "JSON parse error: Numeric value (3627723672367) out of range of int\n" +
                " at [Source: (PushbackInputStream); line: 12, column: 38]; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Numeric value (3627723672367) out of range of int\n" +
                " at [Source: (PushbackInputStream); line: 12, column: 38]\n" +
                " at [Source: (PushbackInputStream); line: 12, column: 25] (through reference chain: com.meesho.pricing.data.request.ProductSupplierPriceMapRequest[\"data\"]->java.util.ArrayList[0]->com.meesho.pricing.data.request.ProductSupplierPriceMapObject[\"shipping_charges\"])");
    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with Max monetization_type", priority = 36)
    public void PriceInsertMax_Monetization_typePricePostCall() throws Exception {
        String payload = PriceInsertClient.createMax_Monetization_typePriceInsertPayload(supplier_id, product_id, variation_id, price, mrp_price,
                500, 600, 7000, 300, 400,
                215, "326766326623", 5, 3);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("error"), "Bad Request");
        Assert.assertEquals(response.jsonPath().get("message"), "JSON parse error: Numeric value (326766326623) out of range of int\n" +
                " at [Source: (PushbackInputStream); line: 13, column: 37]; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Numeric value (326766326623) out of range of int\n" +
                " at [Source: (PushbackInputStream); line: 13, column: 37]\n" +
                " at [Source: (PushbackInputStream); line: 13, column: 25] (through reference chain: com.meesho.pricing.data.request.ProductSupplierPriceMapRequest[\"data\"]->java.util.ArrayList[0]->com.meesho.pricing.data.request.ProductSupplierPriceMapObject[\"monetization_type\"])");
    }

    @Test(enabled = true, description = "Validate Price Inesrt post call with Max Monetization_Percent", priority = 37)
    public void PriceInsertMax_Monetization_PercentPricePostCall() throws Exception {
        String payload = PriceInsertClient.createMax_Monetization_PercentPriceInsertPayload(supplier_id, product_id, variation_id, price, mrp_price,
                500, 600, 7000, 300, 400,
                215, 123, "236776762323", 3);
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("error"), "Bad Request");
        Assert.assertEquals(response.jsonPath().get("message"), "JSON parse error: Numeric value (236776762323) out of range of int\n" +
                " at [Source: (PushbackInputStream); line: 14, column: 40]; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Numeric value (236776762323) out of range of int\n" +
                " at [Source: (PushbackInputStream); line: 14, column: 40]\n" +
                " at [Source: (PushbackInputStream); line: 14, column: 28] (through reference chain: com.meesho.pricing.data.request.ProductSupplierPriceMapRequest[\"data\"]->java.util.ArrayList[0]->com.meesho.pricing.data.request.ProductSupplierPriceMapObject[\"monetization_percent\"])");
    }
    @Test(enabled = true, description = "Validate Price Inesrt post call with Max Monetization_Percent", priority = 38)
    public void PriceInsertMax_GST_RatePricePostCall() throws Exception {
        String payload = PriceInsertClient.createMax_GST_RatePriceInsertPayload(supplier_id, product_id, variation_id, price, mrp_price,
                500, 600, 7000, 300, 400,
                215, 123, 10, "76327878237");
        System.out.println(payload);
        response = RestUtil.postCall(Constants.PRICING_INSERT, headers, payload, 400);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("error"), "Bad Request");
        Assert.assertEquals(response.jsonPath().get("message"), "JSON parse error: Numeric value (76327878237) out of range of int\n" +
                " at [Source: (PushbackInputStream); line: 15, column: 32]; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Numeric value (76327878237) out of range of int\n" +
                " at [Source: (PushbackInputStream); line: 15, column: 32]\n" +
                " at [Source: (PushbackInputStream); line: 15, column: 21] (through reference chain: com.meesho.pricing.data.request.ProductSupplierPriceMapRequest[\"data\"]->java.util.ArrayList[0]->com.meesho.pricing.data.request.ProductSupplierPriceMapObject[\"gst_rate\"])");
    }


}
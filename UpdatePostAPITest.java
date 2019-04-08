
package com.meesho.price;

import dbutils.MysqlCon;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import restassured.RestUtil;
import utility.Constants;
import utility.PriceInsertClient;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by lokeshmadivala on 05/04/19.
 */

public class UpdatePostAPITest extends InsertPostAPITest {
    private Response response;
    private static Map<String, String> headers = new HashMap<String, String>();
    MysqlCon mysqlCon = new MysqlCon();
    @BeforeClass
    public void setUp() throws Exception {
        headers.put("Authentication", "test123");
        headers.put("Content-Type", "application/json");
        headers.put("Postman-Token", "7c6a1c24-860d-4cfc-a15f-d67471b11436,64cfd671-83d6-40ea-a5b1-2b60c1491b18");
        headers.put("cache-control", "no-cache,no-cache");
        con = mysqlCon.DatabaseConnector("meesho-supply-v2-db-test-2.cjdqi0zr8ovs.ap-southeast-1.rds.amazonaws.com", "price", "mycroft", "poirot20");
    }
    @Test(enabled = true, description = "Validate Price Update Post call without header", priority = 0)
    public void updatePricePostCallWithoutHeader()
    {
        headers.remove("Authentication");

        String payload = PriceInsertClient.createPriceInsertPayload(supplier_id, product_id, variation_id, 800, 768,
                900, 88, 884, 6, 30,
                70, 5, 7, 3);
        response = RestUtil.postCall(Constants.PRICING_UPDATE, headers,payload,403);
       PriceInsertClient.verifyCallbackError(response, "Forbidden","Access Denied", "/v1/product-price/update");
       headers.put("Authentication","test123");
    }
    @Test(enabled = true, description = "Validate Price Update Post call with wrong header", priority = 1)
    public void updatePricePostCallWithWrongHeader()
    {
        headers.put("Authentication","test1234");
        String payload = PriceInsertClient.createPriceInsertPayload(supplier_id, product_id, variation_id, 800, 768,
                900, 88, 884, 6, 30,
                70, 5, 7, 3);
        response = RestUtil.postCall(Constants.PRICING_UPDATE, headers,payload,403);
        response = RestUtil.postCall(Constants.PRICING_UPDATE, headers,payload,403);
        PriceInsertClient.verifyCallbackError(response, "Forbidden","Access Denied","/v1/product-price/update");
        headers.put("Authentication","test123");
    }
    @Test(enabled = true, description = "Validate Price Update post call with single data", priority = 2)
    public void updatePriceSinglePostCall1() throws Exception {

        ArrayList<String> data = mysqlCon.readDatabaseRecords(con, "Select * from product_supplier_price_map where supplier_id='" + supplier_id + "' and product_id='" + product_id + "' and '" + variation_id + "'");
        if (data == null) {
            String payload = PriceInsertClient.createPriceInsertPayload(supplier_id, product_id, variation_id, 835, 761,
                    9001, 881, 850, 18, 305,
                    700, 59, 70, 33);
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            map.put("supplier_id", supplier_id);
            map.put("product_id", product_id);
            map.put("variation_id", variation_id);
            map.put("price", 835);
            map.put("mrp_price", 761);
            map.put("original_price", 9001);
            map.put("supplier_cost", 881);
            map.put("input_price", 850);
            map.put("input_price_inc_gst", 18);
            map.put("shipping_charges_adjustment_amount", 305);
            map.put("shipping_charges", 700);
            map.put("monetization_type", 59);
            map.put("monetization_percent", 70);
            map.put("gst_rate", 33);
            response = RestUtil.postCall(Constants.PRICING_UPDATE, headers, payload, 200);
            response.prettyPrint();
            System.out.println(response);
            Assert.assertEquals(response.getStatusCode(), 200);
            Assert.assertEquals(response.jsonPath().get("service_message"), "Price data updated successfully for given product, supplier and variation ids");
            Assert.assertEquals(response.jsonPath().get("service_code"), "0001");
            Assert.assertEquals(response.jsonPath().get("number_of_rows_inserted"), 1);

            ArrayList<String> oldData = mysqlCon.readDatabaseRecords(con, "Select * from product_supplier_price_map where supplier_id='" + supplier_id + "' and product_id='" + product_id + "' and '" + variation_id + "'");

            int length = PriceInsertClient.Validate(map, oldData);
            Assert.assertEquals(length, 14);
            String payload1 = PriceInsertClient.createPriceInsertPayload(supplier_id, product_id, variation_id, 840, 750,
                    900, 88, 855, 28, 35,
                    75, 4, 7, 3);
            HashMap<String, Integer> map1 = new HashMap<String, Integer>();
            map1.put("supplier_id", supplier_id);
            map1.put("product_id", product_id);
            map1.put("variation_id", variation_id);
            map1.put("price", 840);
            map1.put("mrp_price", 750);
            map1.put("original_price", 900);
            map1.put("supplier_cost", 88);
            map1.put("input_price", 855);
            map1.put("input_price_inc_gst", 28);
            map1.put("shipping_charges_adjustment_amount", 35);
            map1.put("shipping_charges", 75);
            map1.put("monetization_type", 4);
            map1.put("monetization_percent", 7);
            map1.put("gst_rate", 3);
            response = RestUtil.postCall(Constants.PRICING_UPDATE, headers, payload1, 200);
            response.prettyPrint();
            System.out.println(response);
            Assert.assertEquals(response.getStatusCode(), 200);
            Assert.assertEquals(response.jsonPath().get("service_message"), "Price data updated successfully for given product, supplier and variation ids");
            Assert.assertEquals(response.jsonPath().get("service_code"), "0001");
            Assert.assertEquals(response.jsonPath().get("number_of_rows_inserted"), 1);

            ArrayList<String> newData = mysqlCon.readDatabaseRecords(con, "Select * from product_supplier_price_map where supplier_id='" + supplier_id + "' and product_id='" + product_id + "' and '" + variation_id + "'");

            int length1 = PriceInsertClient.Validate(map1, newData);
            Assert.assertEquals(length1, 14);

        } else {
            String payload = PriceInsertClient.createPriceInsertPayload(supplier_id, product_id, variation_id, 835, 761,
                    9001, 881, 850, 18, 305,
                    700, 59, 70, 33);
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            map.put("supplier_id", supplier_id);
            map.put("product_id", product_id);
            map.put("variation_id", variation_id);
            map.put("price", 835);
            map.put("mrp_price", 761);
            map.put("original_price", 9001);
            map.put("supplier_cost", 881);
            map.put("input_price", 850);
            map.put("input_price_inc_gst", 18);
            map.put("shipping_charges_adjustment_amount", 305);
            map.put("shipping_charges", 700);
            map.put("monetization_type", 59);
            map.put("monetization_percent", 70);
            map.put("gst_rate", 33);
            response = RestUtil.postCall(Constants.PRICING_UPDATE, headers, payload, 200);
            response.prettyPrint();
            System.out.println(response);
            Assert.assertEquals(response.getStatusCode(), 200);
            Assert.assertEquals(response.jsonPath().get("service_message"), "Price data updated successfully for given product, supplier and variation ids");
            Assert.assertEquals(response.jsonPath().get("service_code"), "0001");
            Assert.assertEquals(response.jsonPath().get("number_of_rows_inserted"), 1);
            ArrayList<String> newData = mysqlCon.readDatabaseRecords(con, "Select * from product_supplier_price_map where supplier_id='" + supplier_id + "' and product_id='" + product_id + "' and '" + variation_id + "'");

            int length1 = PriceInsertClient.Validate(map, newData);
            Assert.assertEquals(length1, 14);

        }
    }
        @Test(enabled = true, description = "Validate Price Update post call with Max Length Payload", priority = 3)
        public void updatePriceMax_LengthPostCall() throws Exception {
            ArrayList<String> data = mysqlCon.readDatabaseRecords(con, "Select * from product_supplier_price_map where supplier_id='" + supplier_id + "' and product_id='" + product_id + "' and '" + variation_id + "'");
            if (data == null) {
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
                response = RestUtil.postCall(Constants.PRICING_UPDATE, headers, payload, 200);
                response.prettyPrint();
                System.out.println(response);
                Assert.assertEquals(response.getStatusCode(), 200);
                Assert.assertEquals(response.jsonPath().get("service_message"), "Price data updated successfully for given product, supplier and variation ids");
                Assert.assertEquals(response.jsonPath().get("service_code"), "0001");
                Assert.assertEquals(response.jsonPath().get("number_of_rows_inserted"), 1);
                MysqlCon mysqlCon = new MysqlCon();
                Connection con = mysqlCon.DatabaseConnector("meesho-supply-v2-db-test-2.cjdqi0zr8ovs.ap-southeast-1.rds.amazonaws.com", "price", "mycroft", "poirot20");
                ArrayList<String> oldData = mysqlCon.readDatabaseRecords(con, "Select * from product_supplier_price_map where supplier_id='" + supplier_id2 + "' and product_id='" + product_id2 + "' and '" + variation_id2 + "'");

                int length = PriceInsertClient.Validate(map, oldData);
                Assert.assertEquals(length, 14);

            String payload1 = PriceInsertClient.createPriceInsertPayload(supplier_id2, product_id2, variation_id2, price, mrp_price,
                    900723782, 887832788, 855278378, 2, 358389828,
                    757827828, 472782878, 778787237, 378782878);
            HashMap<String, Integer> map1 = new HashMap<String, Integer>();
            map1.put("supplier_id", supplier_id2);
            map1.put("product_id", product_id2);
            map1.put("variation_id", variation_id2);
            map1.put("price", price);
            map1.put("mrp_price", mrp_price);
            map1.put("original_price", 900723782);
            map1.put("supplier_cost", 887832788);
            map1.put("input_price", 855278378);
            map1.put("input_price_inc_gst", 2);
            map1.put("shipping_charges_adjustment_amount", 358389828);
            map1.put("shipping_charges", 757827828);
            map1.put("monetization_type", 472782878);
            map1.put("monetization_percent", 778787237);
            map1.put("gst_rate", 378782878);
            response = RestUtil.postCall(Constants.PRICING_UPDATE, headers, payload1, 200);
            response.prettyPrint();
            System.out.println(response);
            Assert.assertEquals(response.getStatusCode(), 200);
            Assert.assertEquals(response.jsonPath().get("service_message"), "Price data updated successfully for given product, supplier and variation ids");
            Assert.assertEquals(response.jsonPath().get("service_code"), "0001");
            Assert.assertEquals(response.jsonPath().get("number_of_rows_inserted"), 1);

            ArrayList<String> newData = mysqlCon.readDatabaseRecords(con, "Select * from product_supplier_price_map where supplier_id='" + supplier_id2 + "' and product_id='" + product_id2 + "' and '" + variation_id2 + "'");

            int length1 = PriceInsertClient.Validate(map1, newData);
            Assert.assertEquals(length1, 14);

        }

        else{
        String payload1 = PriceInsertClient.createPriceInsertPayload(supplier_id2, product_id2, variation_id2, price, mrp_price,
                900723782, 887832788, 855278378, 2, 358389828,
                757827828, 472782878, 778787237, 378782878);
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("supplier_id", supplier_id2);
        map.put("product_id", product_id2);
        map.put("variation_id", variation_id2);
        map.put("price", price);
        map.put("mrp_price", mrp_price);
        map.put("original_price", 900723782);
        map.put("supplier_cost", 887832788);
        map.put("input_price", 855278378);
        map.put("input_price_inc_gst", 2);
        map.put("shipping_charges_adjustment_amount", 358389828);
        map.put("shipping_charges", 757827828);
        map.put("monetization_type", 472782878);
        map.put("monetization_percent", 778787237);
        map.put("gst_rate", 378782878);
        response = RestUtil.postCall(Constants.PRICING_UPDATE, headers, payload1, 200);
        response.prettyPrint();
        System.out.println(response);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().get("service_message"), "Price data updated successfully for given product, supplier and variation ids");
        Assert.assertEquals(response.jsonPath().get("service_code"), "0001");
        Assert.assertEquals(response.jsonPath().get("number_of_rows_inserted"), 1);

        ArrayList<String> newData = mysqlCon.readDatabaseRecords(con, "Select * from product_supplier_price_map where supplier_id='" + supplier_id2 + "' and product_id='" + product_id2 + "' and '" + variation_id2 + "'");

        int length1 = PriceInsertClient.Validate(map, newData);
        Assert.assertEquals(length1, 14);
    }

    }
}

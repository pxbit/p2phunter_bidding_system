package cfig.p2phunter.th

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test

import org.junit.Assert.*
import org.slf4j.LoggerFactory
import cfig.p2phunter.th.HunterResp.*

/**
 * Author: cfig (yuyezhong@gmail.com))
 * Created on: 10/5/17
 */
class HunterUserInfoRespTest {
    private val log = LoggerFactory.getLogger(HunterUserInfoResp::class.java)

    @Test
    fun getStatus() {
        val ss = """
        {"status":0,"msg":"OK","count":408,"userinfo":[{"UserId":"1","AccessToken":"0c7ed12b-eb93-41e0-9dfe-e36d9596d133","UserBalance":"645"}]}
"""
        val ss2 = """
{
  "status": 0,
  "msg": "OK",
  "count": 408,
  "userinfo": [
    {
      "UserId": "1",
      "AccessToken": "0c7ed12b-eb93-41e0-9dfe-e36d9596d133",
      "UserBalance": "645"
    },
    {
      "UserId": "11",
      "AccessToken": "422d2305-d2f1-4798-8892-f4851c454c2d",
      "UserBalance": "0"
    },
    {
      "UserId": "13",
      "AccessToken": "e738e1f2-fb32-4cf8-8214-9161a69723a5",
      "UserBalance": "1975"
    },
    {
      "UserId": "15",
      "AccessToken": "8d609469-7816-4ef1-ba44-44b1ca8960ee",
      "UserBalance": "237"
    },
    {
      "UserId": "29",
      "AccessToken": "29317af9-c437-44f3-b4d6-f9c0348e609e",
      "UserBalance": "2312"
    },
    {
      "UserId": "31",
      "AccessToken": "c52d339b-7ebd-489a-90ae-fcbd847026b1",
      "UserBalance": "44"
    },
    {
      "UserId": "37",
      "AccessToken": "a1c6bf2d-0fbd-41e6-b878-adde84ca749e",
      "UserBalance": "34"
    },
    {
      "UserId": "39",
      "AccessToken": "6da7c781-88cc-443c-8034-906333354522",
      "UserBalance": "67"
    },
    {
      "UserId": "41",
      "AccessToken": "ac9b8865-9d73-4c7b-94b0-fea404bca055",
      "UserBalance": "22"
    },
    {
      "UserId": "43",
      "AccessToken": "cdeeb743-b9b5-477d-a78d-4450375708ad",
      "UserBalance": "35"
    },
    {
      "UserId": "49",
      "AccessToken": "2773de27-5704-4497-b530-bba3c03b20c7",
      "UserBalance": "1"
    },
    {
      "UserId": "53",
      "AccessToken": "1282ea37-fa98-478d-8db2-34521422bf51",
      "UserBalance": "0"
    },
    {
      "UserId": "55",
      "AccessToken": "28b8d253-6788-4e46-a928-3ec56025a583",
      "UserBalance": "243"
    },
    {
      "UserId": "57",
      "AccessToken": "c5ce9e12-4b8e-4617-af14-f7136017db08",
      "UserBalance": "5155"
    },
    {
      "UserId": "61",
      "AccessToken": "05b7139f-f7c2-4e52-acef-be3807c6b502",
      "UserBalance": "49"
    },
    {
      "UserId": "63",
      "AccessToken": "c36e6f0c-03eb-4bcd-bd6f-b78c2c725a1d",
      "UserBalance": "0"
    },
    {
      "UserId": "65",
      "AccessToken": "478153eb-5e45-405f-8199-de5f0b19e70b",
      "UserBalance": "8"
    },
    {
      "UserId": "71",
      "AccessToken": "65acdb91-a56b-4c4f-a410-ca65cbb22510",
      "UserBalance": "8"
    },
    {
      "UserId": "73",
      "AccessToken": "4d9ea8ec-f5be-4aab-a5d3-706f64f323c1",
      "UserBalance": "2240"
    },
    {
      "UserId": "77",
      "AccessToken": "4be21b0b-9e00-443a-b528-56181b0cec02",
      "UserBalance": "686"
    },
    {
      "UserId": "81",
      "AccessToken": "ddadbabe-132f-4eb8-bf31-e3e82b346841",
      "UserBalance": "1071"
    },
    {
      "UserId": "83",
      "AccessToken": "28098435-4859-40c0-a5e3-6eccac75aa27",
      "UserBalance": "371"
    },
    {
      "UserId": "89",
      "AccessToken": "730c495c-b46c-44b8-a3e8-ea56f2f87293",
      "UserBalance": "27"
    },
    {
      "UserId": "91",
      "AccessToken": "d1d5f873-5ecd-46b7-b21a-e116605ff4b1",
      "UserBalance": "1420"
    },
    {
      "UserId": "95",
      "AccessToken": "d55f08da-7cc1-454b-accb-f67be8204e27",
      "UserBalance": "79"
    },
    {
      "UserId": "97",
      "AccessToken": "b080a1e2-e272-415d-a7e7-e5db21ff4225",
      "UserBalance": "3828"
    },
    {
      "UserId": "99",
      "AccessToken": "df18f9a8-a8a5-470b-a252-b8fdd886b380",
      "UserBalance": "370"
    },
    {
      "UserId": "101",
      "AccessToken": "daaedff5-011e-423c-a8d9-648231d56105",
      "UserBalance": "206"
    },
    {
      "UserId": "105",
      "AccessToken": "6cc79bf9-7a22-4a7a-9e0d-c5d3c81d140e",
      "UserBalance": "13"
    },
    {
      "UserId": "107",
      "AccessToken": "cba4dfa7-318f-4506-bef9-12073fd61849",
      "UserBalance": "11501"
    },
    {
      "UserId": "111",
      "AccessToken": "7ebdfe2a-2944-48dc-9c26-bd2b17b489d2",
      "UserBalance": "1499"
    },
    {
      "UserId": "113",
      "AccessToken": "4c03c5ee-1d89-4feb-838c-8893d77bccb4",
      "UserBalance": "172"
    },
    {
      "UserId": "115",
      "AccessToken": "60fa7003-80c3-4389-ac82-b470836a6986",
      "UserBalance": "1862"
    },
    {
      "UserId": "117",
      "AccessToken": "2a787230-0303-4e6f-a50b-f1a6fbc909c8",
      "UserBalance": "3257"
    },
    {
      "UserId": "119",
      "AccessToken": "e0d1d514-326a-460e-959c-0df9b421020e",
      "UserBalance": "2871"
    },
    {
      "UserId": "121",
      "AccessToken": "26bd10e0-7e7c-454d-a8a6-4002f53e822f",
      "UserBalance": "3329"
    },
    {
      "UserId": "123",
      "AccessToken": "fd6bc6c4-7266-4200-970d-a1c23e8fb8fb",
      "UserBalance": "31"
    },
    {
      "UserId": "209",
      "AccessToken": "2d8efe26-5356-4aec-9630-985012c7755e",
      "UserBalance": "547"
    },
    {
      "UserId": "129",
      "AccessToken": "fe2654b0-7a9c-4489-a375-643ab8aaf026",
      "UserBalance": "7910"
    },
    {
      "UserId": "131",
      "AccessToken": "7ce82062-f2f1-438f-ad86-6d9c462f7c64",
      "UserBalance": "411"
    },
    {
      "UserId": "135",
      "AccessToken": "515e0806-f2a8-4d38-9e5b-dd4b677aafee",
      "UserBalance": "3103"
    },
    {
      "UserId": "141",
      "AccessToken": "aedc1094-0c74-4b89-b4f5-37632eb39b8d",
      "UserBalance": "9827"
    },
    {
      "UserId": "143",
      "AccessToken": "f9883882-1f5c-4df7-af93-bed41265ceb6",
      "UserBalance": "84"
    },
    {
      "UserId": "145",
      "AccessToken": "f4655c39-5541-4851-96f3-7031444a366c",
      "UserBalance": "6648"
    },
    {
      "UserId": "147",
      "AccessToken": "180633b7-b06b-4a82-9d71-01f8154ef4be",
      "UserBalance": "446"
    },
    {
      "UserId": "149",
      "AccessToken": "33a40ba7-311c-4803-8db8-9fe4e48e8f29",
      "UserBalance": "562"
    },
    {
      "UserId": "151",
      "AccessToken": "5ac6bad6-0f07-4027-a4e5-95ef7633d1eb",
      "UserBalance": "398"
    },
    {
      "UserId": "183",
      "AccessToken": "03a60efd-758a-4d97-8bdb-ff4ddd853b22",
      "UserBalance": "766"
    },
    {
      "UserId": "155",
      "AccessToken": "ff0e08dd-c50a-442e-a118-f60763eb4722",
      "UserBalance": "478"
    },
    {
      "UserId": "157",
      "AccessToken": "13dc9abd-372e-467b-b825-4a33578a4c25",
      "UserBalance": "2483"
    },
    {
      "UserId": "159",
      "AccessToken": "854d75d3-69aa-40dc-985d-03b7003cfaf2",
      "UserBalance": "127665"
    },
    {
      "UserId": "161",
      "AccessToken": "b4e2b372-7b29-4aab-924f-00213315a2ab",
      "UserBalance": "457"
    },
    {
      "UserId": "163",
      "AccessToken": "6d51be73-28d4-4077-8963-5a411f16886b",
      "UserBalance": "311"
    },
    {
      "UserId": "165",
      "AccessToken": "840bb61a-dff2-4203-a19a-8df31ba2af3e",
      "UserBalance": "38"
    },
    {
      "UserId": "175",
      "AccessToken": "8bd78e5d-eea6-4a3c-ae08-d825f03b9239",
      "UserBalance": "5274"
    },
    {
      "UserId": "177",
      "AccessToken": "125d0513-1cd9-41c4-9592-a9223620e0a6",
      "UserBalance": "78"
    },
    {
      "UserId": "179",
      "AccessToken": "4acadce0-0e17-4be0-ae15-906c9c9977e0",
      "UserBalance": "392"
    },
    {
      "UserId": "281",
      "AccessToken": "f32ebe7b-adfc-4053-a436-bc0b31175ca3",
      "UserBalance": "300"
    },
    {
      "UserId": "185",
      "AccessToken": "aaf651d5-62b0-489a-9bb7-406d9ad64ed3",
      "UserBalance": "9490"
    },
    {
      "UserId": "189",
      "AccessToken": "618abd58-2448-4963-a279-6de65b6e527d",
      "UserBalance": "3009"
    },
    {
      "UserId": "191",
      "AccessToken": "cbe42839-f5c4-4531-9a31-64ef83845203",
      "UserBalance": "996"
    },
    {
      "UserId": "277",
      "AccessToken": "f6046c3e-5ed5-4dfe-b998-7cbca8fdbdd3",
      "UserBalance": "38"
    },
    {
      "UserId": "195",
      "AccessToken": "33d568da-60b4-4b10-bda3-6e6e1ce9cdb1",
      "UserBalance": "2346"
    },
    {
      "UserId": "197",
      "AccessToken": "bf50c0a6-9ab7-4764-94dd-94cbc338617f",
      "UserBalance": "5279"
    },
    {
      "UserId": "201",
      "AccessToken": "b6202470-35b3-4d52-bd7d-505810255924",
      "UserBalance": "23"
    },
    {
      "UserId": "203",
      "AccessToken": "8a1a22d8-a459-4b81-a76a-184edfa6e2df",
      "UserBalance": "20"
    },
    {
      "UserId": "219",
      "AccessToken": "7bc820eb-8994-44a4-837f-e9bb4cdb26df",
      "UserBalance": "169"
    },
    {
      "UserId": "211",
      "AccessToken": "8c1c3ff6-789c-4a9a-8edb-081a28afc0d3",
      "UserBalance": "25364"
    },
    {
      "UserId": "217",
      "AccessToken": "47902799-5b8e-4b3f-a1ed-2c1a9c8d79b0",
      "UserBalance": "127"
    },
    {
      "UserId": "263",
      "AccessToken": "fbef294d-da65-4931-a49b-83f2ce90bf0c",
      "UserBalance": "44"
    },
    {
      "UserId": "223",
      "AccessToken": "c5e8f0ee-14e7-4812-ba64-e18548486cd3",
      "UserBalance": "509"
    },
    {
      "UserId": "225",
      "AccessToken": "10c8ebdf-ae1e-4c5e-b805-661cf34401b2",
      "UserBalance": "11064"
    },
    {
      "UserId": "227",
      "AccessToken": "5ba4af6a-0d6f-4b1f-9eee-38106907ca56",
      "UserBalance": "47"
    },
    {
      "UserId": "237",
      "AccessToken": "429ca0f6-854f-4a9e-8afd-128d95ca0473",
      "UserBalance": "9079"
    },
    {
      "UserId": "289",
      "AccessToken": "aa0195a0-bef2-472d-bf23-b4d87adb9e76",
      "UserBalance": "111"
    },
    {
      "UserId": "247",
      "AccessToken": "0148519b-53fb-408a-b0b6-5b3d28a8a802",
      "UserBalance": "22935"
    },
    {
      "UserId": "257",
      "AccessToken": "c116069f-9208-4ea8-9917-0f9684b6472a",
      "UserBalance": "39"
    },
    {
      "UserId": "619",
      "AccessToken": "ecf84eab-bd0e-4f19-b358-5e41fe7d8413",
      "UserBalance": "415"
    },
    {
      "UserId": "261",
      "AccessToken": "6ff3a240-39ec-4a9a-a03f-1ec60fde3f0f",
      "UserBalance": "30269"
    },
    {
      "UserId": "527",
      "AccessToken": "750b894b-7237-4186-8800-eec4140ce1f5",
      "UserBalance": "4304"
    },
    {
      "UserId": "269",
      "AccessToken": "e9d464d7-6f47-461c-9176-fd16dda994d2",
      "UserBalance": "8"
    },
    {
      "UserId": "271",
      "AccessToken": "74b2d86f-6026-49f0-9c20-b5e66f4ee9ec",
      "UserBalance": "402"
    },
    {
      "UserId": "273",
      "AccessToken": "8344f1dd-c213-4d7f-a706-59e0b5d10351",
      "UserBalance": "15398"
    },
    {
      "UserId": "275",
      "AccessToken": "6fe33b2f-ec09-4185-bb6c-aa8e5cf476e2",
      "UserBalance": "276"
    },
    {
      "UserId": "279",
      "AccessToken": "64935f13-16b3-4ddf-9f2e-785384df5db9",
      "UserBalance": "250"
    },
    {
      "UserId": "285",
      "AccessToken": "9c56987a-4d27-4ea5-bdd0-fc49e83c27cb",
      "UserBalance": "383439"
    },
    {
      "UserId": "287",
      "AccessToken": "13874b57-9a07-46ae-84a1-1bd46fc2ad8d",
      "UserBalance": "44"
    },
    {
      "UserId": "291",
      "AccessToken": "cd5c6019-1238-49c7-a4e7-707a0a651447",
      "UserBalance": "2315"
    },
    {
      "UserId": "297",
      "AccessToken": "f35b9e33-67a1-4131-8dc3-eeae7d3162a4",
      "UserBalance": "45"
    },
    {
      "UserId": "299",
      "AccessToken": "d3a01f57-1620-4290-8092-e4e3aaa79c3b",
      "UserBalance": "3633"
    },
    {
      "UserId": "301",
      "AccessToken": "bf05c191-81be-4f6b-9dfa-617a28b4f16c",
      "UserBalance": "5280"
    },
    {
      "UserId": "303",
      "AccessToken": "3cbd75a3-100b-4042-8097-d0dac88cc6c5",
      "UserBalance": "44"
    },
    {
      "UserId": "307",
      "AccessToken": "1fc9a57c-4bc3-4d1d-9df9-ced6bee4f11c",
      "UserBalance": "582"
    },
    {
      "UserId": "309",
      "AccessToken": "a256e61e-e955-4f6a-98f8-82e2ac6402e1",
      "UserBalance": "28"
    },
    {
      "UserId": "315",
      "AccessToken": "5bdc66fd-7761-4f3f-acde-ae28a0798377",
      "UserBalance": "19"
    },
    {
      "UserId": "317",
      "AccessToken": "6409b3f0-97e2-463e-a4b1-b6934a361189",
      "UserBalance": "1130"
    },
    {
      "UserId": "319",
      "AccessToken": "4ac1cb05-e5df-44b3-a6f5-f136bd317075",
      "UserBalance": "1383"
    },
    {
      "UserId": "323",
      "AccessToken": "bca9b2f0-0499-4335-8eb1-b39ea9bba630",
      "UserBalance": "2428"
    },
    {
      "UserId": "325",
      "AccessToken": "e7a71746-9e70-45a0-9ae8-313d13a5ee6f",
      "UserBalance": "111"
    },
    {
      "UserId": "331",
      "AccessToken": "4da6dfb2-ff71-4eac-82d2-6f64dfbbee63",
      "UserBalance": "203"
    },
    {
      "UserId": "333",
      "AccessToken": "9df62b1d-1122-4cc8-b875-8b317837814f",
      "UserBalance": "75"
    },
    {
      "UserId": "339",
      "AccessToken": "5192afb9-2d94-40a7-9fc1-a53a0a3838ea",
      "UserBalance": "203"
    },
    {
      "UserId": "343",
      "AccessToken": "42427254-411c-4d6a-b2c2-e75afce770a4",
      "UserBalance": "32"
    },
    {
      "UserId": "345",
      "AccessToken": "e3ce696a-099d-461b-8aaa-92ccc670b6be",
      "UserBalance": "0"
    },
    {
      "UserId": "347",
      "AccessToken": "b1e51418-ccab-4bd4-96f4-316c2c273294",
      "UserBalance": "87"
    },
    {
      "UserId": "349",
      "AccessToken": "4eb65465-fe55-4943-a1ef-a72d6959b681",
      "UserBalance": "1"
    },
    {
      "UserId": "351",
      "AccessToken": "d441bdfc-024b-473a-bc48-630286104626",
      "UserBalance": "66"
    },
    {
      "UserId": "353",
      "AccessToken": "106592b4-dfa4-426a-99a8-36ab554717be",
      "UserBalance": "29746"
    },
    {
      "UserId": "357",
      "AccessToken": "dba1823c-bf7c-45ce-acff-9f330c00c182",
      "UserBalance": "3310"
    },
    {
      "UserId": "595",
      "AccessToken": "0d78f8da-d79a-42a6-a439-c598d3c7430d",
      "UserBalance": "0"
    },
    {
      "UserId": "365",
      "AccessToken": "eceece38-8235-4820-8e14-dc18fd342710",
      "UserBalance": "1168"
    },
    {
      "UserId": "359",
      "AccessToken": "7e109caf-4e64-4b05-aafe-970c594a70a4",
      "UserBalance": "0"
    },
    {
      "UserId": "541",
      "AccessToken": "4b220427-09ce-4e11-a4ac-4044aa9fcade",
      "UserBalance": "37"
    },
    {
      "UserId": "363",
      "AccessToken": "4e3b03c6-3310-4906-a6ce-57e110660887",
      "UserBalance": "3136"
    },
    {
      "UserId": "369",
      "AccessToken": "f28d7f51-4b75-45b8-974f-b5713ed28599",
      "UserBalance": "5"
    },
    {
      "UserId": "371",
      "AccessToken": "181ae155-9e4a-4eb1-9548-5f25f52a790b",
      "UserBalance": "248"
    },
    {
      "UserId": "373",
      "AccessToken": "e3f7e2bc-d8c0-4914-969d-91ea4ef4278a",
      "UserBalance": "11060"
    },
    {
      "UserId": "375",
      "AccessToken": "4ec09c10-ebfa-4422-a49e-0e3cc38df55e",
      "UserBalance": "65"
    },
    {
      "UserId": "377",
      "AccessToken": "ded577c4-220d-45df-8fc6-fc3dff7d0bd5",
      "UserBalance": "281"
    },
    {
      "UserId": "379",
      "AccessToken": "1f3f9bdc-e97e-4940-924c-4062c6cc802b",
      "UserBalance": "122"
    },
    {
      "UserId": "611",
      "AccessToken": "1e9fb874-e2c2-4c2d-b630-b644d649f68a",
      "UserBalance": "3543"
    },
    {
      "UserId": "381",
      "AccessToken": "a8657813-a873-46b4-a87c-b82f4db95935",
      "UserBalance": "25"
    },
    {
      "UserId": "591",
      "AccessToken": "1c068054-7fb5-4525-a74d-d1077d8da987",
      "UserBalance": "4708"
    },
    {
      "UserId": "383",
      "AccessToken": "b46f6f58-a660-41d5-98e0-087a3948a6bb",
      "UserBalance": "98"
    },
    {
      "UserId": "385",
      "AccessToken": "b74c63b0-997b-4ea3-a6dc-3068ff289f7a",
      "UserBalance": "36"
    },
    {
      "UserId": "387",
      "AccessToken": "8ff6f200-8452-4bdb-b852-d975ef17d4ae",
      "UserBalance": "79"
    },
    {
      "UserId": "389",
      "AccessToken": "f8193e6b-3a72-42a7-a539-c7594e8bfb30",
      "UserBalance": "398"
    },
    {
      "UserId": "399",
      "AccessToken": "897a67b1-e61c-4ce4-a1cf-a0a5df8e5439",
      "UserBalance": "0"
    },
    {
      "UserId": "393",
      "AccessToken": "1647449c-9756-4931-a7d7-e7652fc6146b",
      "UserBalance": "0"
    },
    {
      "UserId": "395",
      "AccessToken": "a15ac8e8-6d5d-46d6-be3b-2837a9504c53",
      "UserBalance": "1136"
    },
    {
      "UserId": "401",
      "AccessToken": "e3566c9f-9f26-4ec0-bb13-94b6d912c8d5",
      "UserBalance": "1215"
    },
    {
      "UserId": "403",
      "AccessToken": "afe013cb-e131-4f0a-9320-00bfaa65071e",
      "UserBalance": "749"
    },
    {
      "UserId": "407",
      "AccessToken": "3508dfe5-300e-4d0f-9656-f583a8f39202",
      "UserBalance": "54"
    },
    {
      "UserId": "409",
      "AccessToken": "a4537f4d-82ed-48c4-bdc1-891ac7eec43e",
      "UserBalance": "1"
    },
    {
      "UserId": "411",
      "AccessToken": "a15377b7-036b-4ddf-bd5f-61e327756451",
      "UserBalance": "9394"
    },
    {
      "UserId": "413",
      "AccessToken": "be2b64b2-7052-4215-925e-4937359f09d1",
      "UserBalance": "90"
    },
    {
      "UserId": "415",
      "AccessToken": "7349fa7d-12b8-4a1b-bb64-70a8be64688c",
      "UserBalance": "264"
    },
    {
      "UserId": "417",
      "AccessToken": "d7f37040-f91c-4db2-9aa0-23c30c7ba848",
      "UserBalance": "3541"
    },
    {
      "UserId": "419",
      "AccessToken": "16350393-854e-4dd6-9426-9c85bac71827",
      "UserBalance": "1144"
    },
    {
      "UserId": "421",
      "AccessToken": "2ba264f8-975d-4dda-abf2-b9dbe395c954",
      "UserBalance": "83"
    },
    {
      "UserId": "423",
      "AccessToken": "16eae002-3263-44f1-881f-3b2555af2f4a",
      "UserBalance": "4469"
    },
    {
      "UserId": "431",
      "AccessToken": "99f89717-49f2-415c-973d-749b9abbf32c",
      "UserBalance": "17"
    },
    {
      "UserId": "437",
      "AccessToken": "f34528dc-7d9e-450c-a26e-a8b9b139c4e1",
      "UserBalance": "13"
    },
    {
      "UserId": "439",
      "AccessToken": "10f7d458-89a6-428c-b5c7-76d53e9d7ff8",
      "UserBalance": "10"
    },
    {
      "UserId": "535",
      "AccessToken": "8e4bb2e1-ce71-4686-95b8-334b3d013549",
      "UserBalance": "287"
    },
    {
      "UserId": "443",
      "AccessToken": "9c6b0f37-6086-46f9-8fe2-18663aec564e",
      "UserBalance": "1784"
    },
    {
      "UserId": "445",
      "AccessToken": "4f73705d-7a3b-4cc3-9c1c-baf266b5d70e",
      "UserBalance": "131"
    },
    {
      "UserId": "447",
      "AccessToken": "92d4d079-cdc1-4f92-bb6c-423e16a92c49",
      "UserBalance": "68"
    },
    {
      "UserId": "449",
      "AccessToken": "32621fdf-afb9-43b3-af14-23eba89856a6",
      "UserBalance": "37787"
    },
    {
      "UserId": "453",
      "AccessToken": "b971e71e-f7c9-4048-af24-ffd90bed46ae",
      "UserBalance": "3138"
    },
    {
      "UserId": "461",
      "AccessToken": "2a1c3246-ff9f-486e-8d52-1695151127b3",
      "UserBalance": "0"
    },
    {
      "UserId": "455",
      "AccessToken": "9a9898b8-651c-41ee-9991-8a2a293328d5",
      "UserBalance": "1520"
    },
    {
      "UserId": "457",
      "AccessToken": "c6d68525-f559-4035-a222-4dd613d32e0e",
      "UserBalance": "149"
    },
    {
      "UserId": "459",
      "AccessToken": "ff6d5dc1-acb5-47b9-9d1e-5e3ac33e84a1",
      "UserBalance": "42"
    },
    {
      "UserId": "493",
      "AccessToken": "14d13cd9-ccbd-4c60-b7e8-b97efaa1f7a7",
      "UserBalance": "3249"
    },
    {
      "UserId": "465",
      "AccessToken": "2a466310-a53b-44f8-9eb3-098d1f452ab1",
      "UserBalance": "487"
    },
    {
      "UserId": "467",
      "AccessToken": "8e33e014-ed02-44c4-9c94-e2d0a205444e",
      "UserBalance": "8"
    },
    {
      "UserId": "469",
      "AccessToken": "26adec9a-f0cc-4e13-b8e1-625d7176863e",
      "UserBalance": "290"
    },
    {
      "UserId": "473",
      "AccessToken": "7bd693f4-ec8e-4e6f-b4c7-29121f24c456",
      "UserBalance": "7586"
    },
    {
      "UserId": "475",
      "AccessToken": "81ff5991-5a6f-4475-8831-032792a84582",
      "UserBalance": "1521"
    },
    {
      "UserId": "477",
      "AccessToken": "b4cfdf9d-0e55-40b6-89ff-e4ffb5860d3a",
      "UserBalance": "45"
    },
    {
      "UserId": "479",
      "AccessToken": "6c53d9a8-9336-40cc-a43e-9df22778f834",
      "UserBalance": "3959"
    },
    {
      "UserId": "481",
      "AccessToken": "23e0176a-8686-4cde-8b1c-15b047c93e1b",
      "UserBalance": "85"
    },
    {
      "UserId": "483",
      "AccessToken": "e3937cf8-b13b-43eb-a7e2-494a45d22d91",
      "UserBalance": "33"
    },
    {
      "UserId": "485",
      "AccessToken": "91385fbf-64d5-4ab0-8dc5-616196c72090",
      "UserBalance": "376"
    },
    {
      "UserId": "487",
      "AccessToken": "e2e82e93-8fcf-47dd-bfc0-c8cffd12adac",
      "UserBalance": "765"
    },
    {
      "UserId": "489",
      "AccessToken": "fbc7c91d-c10b-4d40-b2ea-13dc72d8a6a1",
      "UserBalance": "9406"
    },
    {
      "UserId": "491",
      "AccessToken": "f9d670c1-0a8d-47d1-8ef5-02cf26dd18b5",
      "UserBalance": "14089"
    },
    {
      "UserId": "495",
      "AccessToken": "01368e87-e60e-4756-aeb3-d6a93d1d4f51",
      "UserBalance": "31"
    },
    {
      "UserId": "497",
      "AccessToken": "f588e9a3-170b-4f38-bdbf-4ecc55a6b391",
      "UserBalance": "7338"
    },
    {
      "UserId": "499",
      "AccessToken": "9aa47532-9956-469e-884d-2b3cf46d9615",
      "UserBalance": "6173"
    },
    {
      "UserId": "501",
      "AccessToken": "476250bc-33a9-4303-aeb6-dbb58e35680b",
      "UserBalance": "12292"
    },
    {
      "UserId": "503",
      "AccessToken": "33d3e9a5-1bdf-4a3e-bbc6-239322857311",
      "UserBalance": "92"
    },
    {
      "UserId": "505",
      "AccessToken": "40b9d8f5-0ae2-409a-aafc-9d01aa6d8770",
      "UserBalance": "7"
    },
    {
      "UserId": "509",
      "AccessToken": "a7260736-0633-4ba9-99e7-db2c9a4e9e58",
      "UserBalance": "153"
    },
    {
      "UserId": "583",
      "AccessToken": "f6f2cb22-3b46-4152-b55e-194ce8ed1ada",
      "UserBalance": "70"
    },
    {
      "UserId": "513",
      "AccessToken": "54147444-25a8-46ca-b15f-55474953dc3a",
      "UserBalance": "492"
    },
    {
      "UserId": "515",
      "AccessToken": "b187cdbc-38b2-4d70-9aa7-1d7d28f47416",
      "UserBalance": "41"
    },
    {
      "UserId": "517",
      "AccessToken": "4e9b2f22-6d9b-4d5c-bf81-55333345684b",
      "UserBalance": "15659"
    },
    {
      "UserId": "519",
      "AccessToken": "319b50a3-09ee-4513-bd43-7cf49462344a",
      "UserBalance": "64"
    },
    {
      "UserId": "521",
      "AccessToken": "f83f835b-8916-452b-8575-a7bd8aa16868",
      "UserBalance": "5468"
    },
    {
      "UserId": "523",
      "AccessToken": "eb543b50-ecdd-483a-add2-13b333d938ac",
      "UserBalance": "22728"
    },
    {
      "UserId": "525",
      "AccessToken": "f89a2b02-45fc-4e3c-bd7d-2eb8d7d6f2a6",
      "UserBalance": "326"
    },
    {
      "UserId": "529",
      "AccessToken": "c9740139-fd99-4579-bf3b-0689fe6b139e",
      "UserBalance": "28"
    },
    {
      "UserId": "531",
      "AccessToken": "96746c57-56d1-4004-a50a-9f96aa71cf6f",
      "UserBalance": "2"
    },
    {
      "UserId": "533",
      "AccessToken": "f1551e46-d7ad-4f5d-be71-e0ffa57a2bc9",
      "UserBalance": "6837"
    },
    {
      "UserId": "537",
      "AccessToken": "c17dd310-900e-42b1-a458-d505920302b0",
      "UserBalance": "0"
    },
    {
      "UserId": "539",
      "AccessToken": "1d5c7758-cb23-499d-a401-de8da0b608c9",
      "UserBalance": "683"
    },
    {
      "UserId": "543",
      "AccessToken": "823d593e-44e0-4134-b52d-1d2e36d29c2e",
      "UserBalance": "423"
    },
    {
      "UserId": "545",
      "AccessToken": "7311e9ac-a63a-417a-8ea7-9401aa331942",
      "UserBalance": "2948"
    },
    {
      "UserId": "547",
      "AccessToken": "a54cd3b3-d043-48bc-982f-55b80cd56de4",
      "UserBalance": "343"
    },
    {
      "UserId": "549",
      "AccessToken": "6900be28-46dc-479e-841c-b920bd28918c",
      "UserBalance": "172"
    },
    {
      "UserId": "551",
      "AccessToken": "856a3606-cd5d-43a5-95a2-2296eccabfde",
      "UserBalance": "30"
    },
    {
      "UserId": "555",
      "AccessToken": "923d7685-13e4-4556-9560-bc2affb3ec27",
      "UserBalance": "5697"
    },
    {
      "UserId": "705",
      "AccessToken": "a2fed346-942b-4dd8-81f5-d3628d1831b0",
      "UserBalance": "0"
    },
    {
      "UserId": "557",
      "AccessToken": "8d69b95b-9286-4def-9218-0ce1814cd969",
      "UserBalance": "53"
    },
    {
      "UserId": "559",
      "AccessToken": "fb44a73e-418d-4c2e-92cc-95aaf5492d98",
      "UserBalance": "89"
    },
    {
      "UserId": "561",
      "AccessToken": "a5593667-1093-406e-b182-7bb7faa7ea19",
      "UserBalance": "0"
    },
    {
      "UserId": "563",
      "AccessToken": "910ab7ce-fa84-4df2-912d-e0e53a88039a",
      "UserBalance": "4217"
    },
    {
      "UserId": "565",
      "AccessToken": "9950ee35-3611-4575-9665-585870deeacc",
      "UserBalance": "92"
    },
    {
      "UserId": "567",
      "AccessToken": "f8e817cc-454a-4197-a518-df07e3b8515a",
      "UserBalance": "16036"
    },
    {
      "UserId": "569",
      "AccessToken": "b81e3388-f0e9-4727-a325-c3bc68a3568c",
      "UserBalance": "16"
    },
    {
      "UserId": "571",
      "AccessToken": "95f69112-6352-494b-82ab-bcb1d3cc4cf2",
      "UserBalance": "3"
    },
    {
      "UserId": "573",
      "AccessToken": "d5379ad8-ae73-4577-bd53-42d28f4712b6",
      "UserBalance": "3257"
    },
    {
      "UserId": "575",
      "AccessToken": "c95b4372-e4a9-4089-baab-630d4f60e997",
      "UserBalance": "12"
    },
    {
      "UserId": "577",
      "AccessToken": "e31253b4-84b2-4adc-9f02-4f9e5750d961",
      "UserBalance": "3177"
    },
    {
      "UserId": "603",
      "AccessToken": "bff0ce9a-9ec0-4c14-9672-bb77e513dd2a",
      "UserBalance": "47"
    },
    {
      "UserId": "587",
      "AccessToken": "8780dc11-314b-4b1a-a757-084421cbf4b0",
      "UserBalance": "0"
    },
    {
      "UserId": "579",
      "AccessToken": "06209e5b-5c8c-4332-897a-73121ac200d4",
      "UserBalance": "290"
    },
    {
      "UserId": "581",
      "AccessToken": "d173cb9d-745f-4a0f-96c8-d6dd820b9086",
      "UserBalance": "66103"
    },
    {
      "UserId": "593",
      "AccessToken": "2c170f3d-c66e-4bb4-b715-0d0ed397fc8a",
      "UserBalance": "5284"
    },
    {
      "UserId": "597",
      "AccessToken": "58e34800-5f05-4dca-bd27-f46816645c00",
      "UserBalance": "1293"
    },
    {
      "UserId": "599",
      "AccessToken": "aa248b13-183d-412c-a177-e6f601644637",
      "UserBalance": "221"
    },
    {
      "UserId": "605",
      "AccessToken": "aab85b3d-3b18-42e1-b3dc-1ddcbe2af1b7",
      "UserBalance": "8081"
    },
    {
      "UserId": "609",
      "AccessToken": "58ee5eb9-974c-4839-a41d-e946a1f7ab41",
      "UserBalance": "387"
    },
    {
      "UserId": "719",
      "AccessToken": "94ab877d-5451-4c7c-af96-ed7a607f710b",
      "UserBalance": "284"
    },
    {
      "UserId": "613",
      "AccessToken": "85f37d61-65c4-4d2e-bba1-3e1fa8ed14bd",
      "UserBalance": "259"
    },
    {
      "UserId": "615",
      "AccessToken": "ad697073-70e8-4aaf-a848-1fc0786152cb",
      "UserBalance": "583"
    },
    {
      "UserId": "791",
      "AccessToken": "fe5077c8-98cd-4d95-981b-a3c019215603",
      "UserBalance": "119"
    },
    {
      "UserId": "621",
      "AccessToken": "1e3726f6-9693-4510-b8ea-beb29bce014b",
      "UserBalance": "2653"
    },
    {
      "UserId": "623",
      "AccessToken": "d85baae2-0146-4480-82ea-3d435d441b99",
      "UserBalance": "45"
    },
    {
      "UserId": "625",
      "AccessToken": "408220fa-62e2-41e2-94aa-a32f86d5e15b",
      "UserBalance": "32"
    },
    {
      "UserId": "627",
      "AccessToken": "36bc5c93-6d83-4d57-89ac-d0070523cd26",
      "UserBalance": "6983"
    },
    {
      "UserId": "629",
      "AccessToken": "6756f46b-4384-48f5-9201-919eb93150ab",
      "UserBalance": "62"
    },
    {
      "UserId": "631",
      "AccessToken": "9d4f2ab3-2756-4828-a4b4-5b13537f1795",
      "UserBalance": "17"
    },
    {
      "UserId": "637",
      "AccessToken": "f2ded757-52e3-485a-a765-2b5746b80490",
      "UserBalance": "50"
    },
    {
      "UserId": "981",
      "AccessToken": "ab18fa53-fe34-4a3f-b4fa-5aed3e5b6248",
      "UserBalance": "1752"
    },
    {
      "UserId": "639",
      "AccessToken": "acc9ad30-6f08-4c2e-b89d-37a71ea04adf",
      "UserBalance": "9659"
    },
    {
      "UserId": "641",
      "AccessToken": "c44eb79c-b983-4bc8-9ba4-c54377525c72",
      "UserBalance": "4480"
    },
    {
      "UserId": "643",
      "AccessToken": "7747734e-1e9e-42e6-8394-741ed1d2ac05",
      "UserBalance": "35"
    },
    {
      "UserId": "645",
      "AccessToken": "9241bb04-89e8-47f4-a5de-37df1ac8c7d1",
      "UserBalance": "17994"
    },
    {
      "UserId": "647",
      "AccessToken": "f17a18e8-83b0-429b-97e4-9c99489229b7",
      "UserBalance": "59"
    },
    {
      "UserId": "649",
      "AccessToken": "fb7a1e90-43d3-4c69-8241-ed0e00cde6f7",
      "UserBalance": "14665"
    },
    {
      "UserId": "651",
      "AccessToken": "11a13583-814a-4b34-9dc1-30cd5409ac78",
      "UserBalance": "7"
    },
    {
      "UserId": "653",
      "AccessToken": "8f67fa90-dd4a-4ea7-9fcf-c914b189837f",
      "UserBalance": "319"
    },
    {
      "UserId": "655",
      "AccessToken": "cb1f3a2c-40a5-4d6b-af4a-74516b8d52f5",
      "UserBalance": "59"
    },
    {
      "UserId": "657",
      "AccessToken": "52a3fdb3-1f5e-4999-a542-efcf85085466",
      "UserBalance": "933"
    },
    {
      "UserId": "659",
      "AccessToken": "e2528060-e33a-45eb-9bb3-8881822ac324",
      "UserBalance": "152"
    },
    {
      "UserId": "661",
      "AccessToken": "7047dd78-d41a-4436-972b-641d64a5d99a",
      "UserBalance": "28"
    },
    {
      "UserId": "663",
      "AccessToken": "e43e812c-ce28-4d8b-825d-41330e33931d",
      "UserBalance": "370"
    },
    {
      "UserId": "665",
      "AccessToken": "0ba64757-5b3d-4c31-ac68-a3cd768666aa",
      "UserBalance": "152"
    },
    {
      "UserId": "667",
      "AccessToken": "514e2e58-a4f9-4796-9142-9a36fbc07d95",
      "UserBalance": "2023"
    },
    {
      "UserId": "681",
      "AccessToken": "c8f00597-da48-4d54-976c-c5da0c051824",
      "UserBalance": "50"
    },
    {
      "UserId": "669",
      "AccessToken": "e4c1ea0c-9dee-4526-a003-9ceef93c5875",
      "UserBalance": "358"
    },
    {
      "UserId": "673",
      "AccessToken": "679b73d9-dd37-4329-8cee-f0605d464439",
      "UserBalance": "55"
    },
    {
      "UserId": "677",
      "AccessToken": "46681ede-a4d3-480d-83d8-acb0d22018e0",
      "UserBalance": "511"
    },
    {
      "UserId": "679",
      "AccessToken": "716483be-55d3-46e5-a4c0-747b8d685ca2",
      "UserBalance": "135"
    },
    {
      "UserId": "683",
      "AccessToken": "6be43e55-1cbc-495a-bddc-aeedd6f3fc17",
      "UserBalance": "7610"
    },
    {
      "UserId": "689",
      "AccessToken": "a8898f9f-748a-42a8-9793-1204c05d301e",
      "UserBalance": "21284"
    },
    {
      "UserId": "691",
      "AccessToken": "429cf4eb-85b2-4994-9ca7-1bce49ca775e",
      "UserBalance": "2627"
    },
    {
      "UserId": "693",
      "AccessToken": "4524bf0c-f5be-4901-bf50-fca9631d9c3a",
      "UserBalance": "10"
    },
    {
      "UserId": "695",
      "AccessToken": "fab078ff-b486-4686-a856-ee7f43f99e55",
      "UserBalance": "401"
    },
    {
      "UserId": "697",
      "AccessToken": "a68b0faf-031b-492f-8116-3ca368eed3c1",
      "UserBalance": "0"
    },
    {
      "UserId": "699",
      "AccessToken": "3da9b5e9-dea0-4c44-9c57-1f4ab59e586c",
      "UserBalance": "409"
    },
    {
      "UserId": "701",
      "AccessToken": "d62e3d45-cf10-424d-b704-3f6ad17756e1",
      "UserBalance": "0"
    },
    {
      "UserId": "703",
      "AccessToken": "fa2485d6-0dd5-4947-83f9-9e0e70a6d35f",
      "UserBalance": "79"
    },
    {
      "UserId": "955",
      "AccessToken": "baba2082-6d09-45f6-87c1-ff1c8fc10921",
      "UserBalance": "70"
    },
    {
      "UserId": "957",
      "AccessToken": "804787d0-83bd-4e5f-bdfc-10e424719956",
      "UserBalance": "20"
    },
    {
      "UserId": "707",
      "AccessToken": "d14eac48-9134-4f95-8b91-240faa484a26",
      "UserBalance": "33"
    },
    {
      "UserId": "767",
      "AccessToken": "bba19254-dc2d-43d6-a483-e26a4b0c29db",
      "UserBalance": "0"
    },
    {
      "UserId": "715",
      "AccessToken": "8a1876e3-b809-4d22-aa87-a7b491c239d2",
      "UserBalance": "58"
    },
    {
      "UserId": "717",
      "AccessToken": "93241a63-ea7c-475e-b4b3-c9030338791d",
      "UserBalance": "30"
    },
    {
      "UserId": "721",
      "AccessToken": "0b37b6ae-1485-4c92-9686-db857a0276f6",
      "UserBalance": "185"
    },
    {
      "UserId": "723",
      "AccessToken": "e5d9fda6-92e9-4a8e-aeef-85c017821687",
      "UserBalance": "14734"
    },
    {
      "UserId": "727",
      "AccessToken": "c1654fcc-f477-47b2-b5ce-6fdd7f5a5715",
      "UserBalance": "187"
    },
    {
      "UserId": "731",
      "AccessToken": "a0674fc3-435b-4a83-9366-b6f12e72d9bc",
      "UserBalance": "1965"
    },
    {
      "UserId": "733",
      "AccessToken": "e7e73726-07fa-4228-a1cf-e71255910548",
      "UserBalance": "40068"
    },
    {
      "UserId": "735",
      "AccessToken": "fd24abe7-46ba-4e93-9e14-e1f839fb6baf",
      "UserBalance": "2245"
    },
    {
      "UserId": "737",
      "AccessToken": "6ba13b0a-4c73-47df-994c-9943f7ed2185",
      "UserBalance": "7817"
    },
    {
      "UserId": "739",
      "AccessToken": "476b0bb4-db69-48af-88cd-524198fa8962",
      "UserBalance": "101"
    },
    {
      "UserId": "741",
      "AccessToken": "efc51b02-3075-4aa3-87f1-90ff82cf3deb",
      "UserBalance": "626"
    },
    {
      "UserId": "743",
      "AccessToken": "9d9ebde5-2e79-4b84-82e1-9ac3661b8714",
      "UserBalance": "145"
    },
    {
      "UserId": "747",
      "AccessToken": "e579570b-33bc-4923-9e8d-b8be9b094663",
      "UserBalance": "1"
    },
    {
      "UserId": "855",
      "AccessToken": "fdfd0fe3-a9ec-47c1-a870-8ea37326a7cf",
      "UserBalance": "37"
    },
    {
      "UserId": "751",
      "AccessToken": "ed9e4b85-bf26-4a38-94cd-48f0fa21c504",
      "UserBalance": "0"
    },
    {
      "UserId": "753",
      "AccessToken": "001978e8-59c7-49af-a68f-4854e967ef6c",
      "UserBalance": "11"
    },
    {
      "UserId": "755",
      "AccessToken": "ec842697-2d58-4232-8400-14d6d7b17be4",
      "UserBalance": "19"
    },
    {
      "UserId": "757",
      "AccessToken": "19e55ec9-b86d-4c74-9ba6-c9eb37a6dc3c",
      "UserBalance": "5577"
    },
    {
      "UserId": "759",
      "AccessToken": "9ce4c4bc-afa6-4647-9c0a-9df82983439c",
      "UserBalance": "42"
    },
    {
      "UserId": "761",
      "AccessToken": "995f9daf-7afd-496e-8c0f-e32b53f1b967",
      "UserBalance": "37663"
    },
    {
      "UserId": "789",
      "AccessToken": "2622f8fa-b795-4abd-9163-763a89795e4c",
      "UserBalance": "23"
    },
    {
      "UserId": "763",
      "AccessToken": "32e5b520-5b90-4f65-9cf8-a2de2a8c3873",
      "UserBalance": "799"
    },
    {
      "UserId": "765",
      "AccessToken": "c9d3c76d-4ae2-44c2-acac-85c4d22c0a47",
      "UserBalance": "2449"
    },
    {
      "UserId": "771",
      "AccessToken": "45602b0f-5a69-41b6-9290-bb0abcc15a71",
      "UserBalance": "6322"
    },
    {
      "UserId": "773",
      "AccessToken": "57b1c07e-baf5-4662-849b-03692082712e",
      "UserBalance": "18"
    },
    {
      "UserId": "775",
      "AccessToken": "9b2b4314-c2a8-4aed-9e2d-d410d61e3d91",
      "UserBalance": "2964"
    },
    {
      "UserId": "779",
      "AccessToken": "6b67c0b4-d1ce-4da4-b207-429cb448c06d",
      "UserBalance": "60"
    },
    {
      "UserId": "781",
      "AccessToken": "edce1ecf-1132-498a-bae0-e31cd7073f00",
      "UserBalance": "1027"
    },
    {
      "UserId": "783",
      "AccessToken": "49ac38ad-0c60-4463-b672-72a504d36deb",
      "UserBalance": "27"
    },
    {
      "UserId": "801",
      "AccessToken": "43ee7a4d-538b-4368-86e0-3ce42a6ca9c5",
      "UserBalance": "8"
    },
    {
      "UserId": "863",
      "AccessToken": "4f01e5d5-07d5-42c6-a470-4e86e90ef0e0",
      "UserBalance": "5716"
    },
    {
      "UserId": "793",
      "AccessToken": "1cfb9281-f5b6-4140-ab8c-f0c6910fef09",
      "UserBalance": "1965"
    },
    {
      "UserId": "795",
      "AccessToken": "c918d67a-c8cf-4d27-a241-649c89044df5",
      "UserBalance": "0"
    },
    {
      "UserId": "797",
      "AccessToken": "0774fb02-d89c-4f7d-ba7b-273338a369ad",
      "UserBalance": "199"
    },
    {
      "UserId": "799",
      "AccessToken": "e7f4ec63-8680-4b5d-9978-b9e957c12dfd",
      "UserBalance": "30"
    },
    {
      "UserId": "803",
      "AccessToken": "6064ae4b-8318-4156-ab54-756e21825b70",
      "UserBalance": "36"
    },
    {
      "UserId": "805",
      "AccessToken": "7e9d62df-65b4-42e1-99ee-11fe05c1810b",
      "UserBalance": "4"
    },
    {
      "UserId": "807",
      "AccessToken": "9f4076ec-d333-4dc4-8a90-6e9b3f294a9b",
      "UserBalance": "860"
    },
    {
      "UserId": "809",
      "AccessToken": "dea19580-52ed-4c58-aec7-91d082df5327",
      "UserBalance": "452"
    },
    {
      "UserId": "813",
      "AccessToken": "18fc08f1-df6c-41d4-a9f9-55835a03ab0d",
      "UserBalance": "96"
    },
    {
      "UserId": "819",
      "AccessToken": "8b6ea069-9584-42d9-a077-1555f5904d88",
      "UserBalance": "203"
    },
    {
      "UserId": "823",
      "AccessToken": "f23760e6-764c-4e90-8016-35413f66eb6e",
      "UserBalance": "4446"
    },
    {
      "UserId": "827",
      "AccessToken": "6e8e51bb-7f74-4186-9c36-a006ab2c9691",
      "UserBalance": "123"
    },
    {
      "UserId": "829",
      "AccessToken": "76b336ea-b7e4-484e-9fb6-d961316bed66",
      "UserBalance": "56"
    },
    {
      "UserId": "831",
      "AccessToken": "2e7147c9-26d6-4f3e-9093-1e71b937d47e",
      "UserBalance": "14"
    },
    {
      "UserId": "833",
      "AccessToken": "da18e148-15b7-4a75-ab23-5dba6f2961cf",
      "UserBalance": "3676"
    },
    {
      "UserId": "835",
      "AccessToken": "5f79207e-e5c0-4867-a21d-f6b3b936d219",
      "UserBalance": "1532"
    },
    {
      "UserId": "837",
      "AccessToken": "ca28b1e7-01ae-46c1-a0ed-13146b288bc3",
      "UserBalance": "16339"
    },
    {
      "UserId": "839",
      "AccessToken": "544f4b18-9dc2-470b-89e3-9896684be8bc",
      "UserBalance": "44449"
    },
    {
      "UserId": "841",
      "AccessToken": "5932a642-5320-4ae0-abb5-0c63c3d4b6d8",
      "UserBalance": "2201"
    },
    {
      "UserId": "843",
      "AccessToken": "e644a5f8-0240-4602-9c4e-f85171f9f0f2",
      "UserBalance": "32"
    },
    {
      "UserId": "845",
      "AccessToken": "924e216b-5917-4efb-b3ae-3a00c335a8d8",
      "UserBalance": "34"
    },
    {
      "UserId": "847",
      "AccessToken": "bec6b835-ae27-4818-93c6-add4af7f7f56",
      "UserBalance": "19"
    },
    {
      "UserId": "851",
      "AccessToken": "7ae5250f-2688-49fc-a189-f0630ab1a1ea",
      "UserBalance": "30"
    },
    {
      "UserId": "853",
      "AccessToken": "c2fe1f79-ff22-46d9-bd1f-51cac7bb2948",
      "UserBalance": "0"
    },
    {
      "UserId": "1005",
      "AccessToken": "5602e1e5-c6ee-4ea3-8d57-7505612b76e2",
      "UserBalance": "4881"
    },
    {
      "UserId": "857",
      "AccessToken": "4945e6a7-2a1e-4d3b-bb27-d800082f6b48",
      "UserBalance": "0"
    },
    {
      "UserId": "861",
      "AccessToken": "4294a8eb-2e36-47ff-90b3-e7f6d0d0cd46",
      "UserBalance": "15"
    },
    {
      "UserId": "865",
      "AccessToken": "4701c17c-7f93-4ce9-8484-7e27b85ba440",
      "UserBalance": "25"
    },
    {
      "UserId": "867",
      "AccessToken": "863814f2-c202-4d7f-a4f1-59e770b16ace",
      "UserBalance": "6860"
    },
    {
      "UserId": "869",
      "AccessToken": "87aed344-1577-46db-a741-3602fb878dce",
      "UserBalance": "13"
    },
    {
      "UserId": "871",
      "AccessToken": "bac5ba8c-02b0-4db2-9489-9847c6c8de0f",
      "UserBalance": "2370"
    },
    {
      "UserId": "877",
      "AccessToken": "365519fb-5beb-4c10-a189-6ffc9be548f3",
      "UserBalance": "614"
    },
    {
      "UserId": "879",
      "AccessToken": "c56cd877-a6cd-47aa-bf0e-5d930062bc0a",
      "UserBalance": "99"
    },
    {
      "UserId": "881",
      "AccessToken": "78cc6933-0b62-4662-971c-1a7d86523876",
      "UserBalance": "73"
    },
    {
      "UserId": "883",
      "AccessToken": "6d5c5ea9-cf4c-477c-91ee-bec88d3a27ac",
      "UserBalance": "798"
    },
    {
      "UserId": "885",
      "AccessToken": "1ffeb011-9dd3-4b57-a376-32c4ebd14061",
      "UserBalance": "77"
    },
    {
      "UserId": "889",
      "AccessToken": "539370e9-76b9-4eca-97c4-d5cb90f3a830",
      "UserBalance": "301"
    },
    {
      "UserId": "891",
      "AccessToken": "4f02997b-b723-454f-9c9c-39739f42383c",
      "UserBalance": "165"
    },
    {
      "UserId": "893",
      "AccessToken": "44c052f4-8d62-4cc6-96cf-9bdea33c5404",
      "UserBalance": "26"
    },
    {
      "UserId": "895",
      "AccessToken": "ede918b0-e5cf-4b0d-8134-a10a618193f3",
      "UserBalance": "75"
    },
    {
      "UserId": "897",
      "AccessToken": "1d00889f-526d-4cfe-888f-ce647b009fad",
      "UserBalance": "9698"
    },
    {
      "UserId": "899",
      "AccessToken": "eeba7586-d3bf-4117-b559-63a1004c46df",
      "UserBalance": "175"
    },
    {
      "UserId": "901",
      "AccessToken": "38936ca2-3c3b-4672-b478-7667dd063cfd",
      "UserBalance": "0"
    },
    {
      "UserId": "905",
      "AccessToken": "2bcf662a-5eae-490f-affc-9eb01bdecbcd",
      "UserBalance": "996"
    },
    {
      "UserId": "907",
      "AccessToken": "afb5eaa8-6bd9-441d-ae4c-c7e40d6b5b0e",
      "UserBalance": "18"
    },
    {
      "UserId": "909",
      "AccessToken": "01bc4c32-c0fc-4e28-91d7-ba23416080ae",
      "UserBalance": "373"
    },
    {
      "UserId": "911",
      "AccessToken": "a6270aac-4cf8-4772-af9d-585ffc8c1ec5",
      "UserBalance": "30"
    },
    {
      "UserId": "913",
      "AccessToken": "47a4e3a7-987e-4479-aeda-54793f91b488",
      "UserBalance": "51"
    },
    {
      "UserId": "915",
      "AccessToken": "89171fff-d6f3-4d2a-b91a-de7cb2c1d27d",
      "UserBalance": "57"
    },
    {
      "UserId": "933",
      "AccessToken": "169c29ef-5c34-4ed3-8001-78c3c396911b",
      "UserBalance": "44"
    },
    {
      "UserId": "919",
      "AccessToken": "622ed8ee-9fbd-48d1-b86d-99cb0c84d578",
      "UserBalance": "0"
    },
    {
      "UserId": "921",
      "AccessToken": "427e7407-182a-43b9-961e-9c3a4b0f1401",
      "UserBalance": "470"
    },
    {
      "UserId": "923",
      "AccessToken": "47ab36ef-df50-477e-b427-648111a355ec",
      "UserBalance": "20"
    },
    {
      "UserId": "925",
      "AccessToken": "a5be5d3b-65df-4baf-8868-237e52fb6a6f",
      "UserBalance": "134"
    },
    {
      "UserId": "927",
      "AccessToken": "0b786c81-bf2b-4a0d-a393-8119da125994",
      "UserBalance": "65"
    },
    {
      "UserId": "929",
      "AccessToken": "df1e34aa-6ee7-45bf-bddd-204aa64be294",
      "UserBalance": "3021"
    },
    {
      "UserId": "931",
      "AccessToken": "33c3c03d-e46a-4c43-885d-f499baa02b85",
      "UserBalance": "2048"
    },
    {
      "UserId": "935",
      "AccessToken": "35787d9d-3a8f-4110-abfd-2fc89bfe40d7",
      "UserBalance": "29"
    },
    {
      "UserId": "937",
      "AccessToken": "bd853800-a04d-43a3-9818-c259ee3fd8c8",
      "UserBalance": "65"
    },
    {
      "UserId": "941",
      "AccessToken": "c81b062a-b41f-4e4c-9d1a-ce05014d5d24",
      "UserBalance": "15"
    },
    {
      "UserId": "943",
      "AccessToken": "3cce8de4-143e-4773-a523-23dd635eafa8",
      "UserBalance": "6"
    },
    {
      "UserId": "945",
      "AccessToken": "9a131401-4e01-4ace-b778-b98ca00aa52f",
      "UserBalance": "8537"
    },
    {
      "UserId": "947",
      "AccessToken": "ecbe655d-4e92-4603-b2a3-17458259c58c",
      "UserBalance": "1563"
    },
    {
      "UserId": "949",
      "AccessToken": "96d09ec3-b71b-4a51-8437-239b0540bdb9",
      "UserBalance": "39"
    },
    {
      "UserId": "953",
      "AccessToken": "f466b3d2-cef8-44ee-b2b2-2218bca0286f",
      "UserBalance": "127"
    },
    {
      "UserId": "951",
      "AccessToken": "200301dc-78c6-4289-86d2-23fa790544b1",
      "UserBalance": "0"
    },
    {
      "UserId": "977",
      "AccessToken": "1a551d61-3474-4599-bd8a-fdf6bb89887c",
      "UserBalance": "27"
    },
    {
      "UserId": "991",
      "AccessToken": "3741076c-b2b6-4149-9daa-3c884591eb97",
      "UserBalance": "1"
    },
    {
      "UserId": "959",
      "AccessToken": "16db79e2-109f-475e-857c-4b9cbfba2585",
      "UserBalance": "95"
    },
    {
      "UserId": "961",
      "AccessToken": "cc2d9991-7480-4165-ad66-da07f4336a93",
      "UserBalance": "126"
    },
    {
      "UserId": "965",
      "AccessToken": "9d098de5-31fe-450e-a39e-ce3981f4bb03",
      "UserBalance": "0"
    },
    {
      "UserId": "969",
      "AccessToken": "9eef4aa8-a05b-470f-98c8-d006582bf2ee",
      "UserBalance": "50"
    },
    {
      "UserId": "973",
      "AccessToken": "84b4e611-4f65-4c7f-8c6d-6dab25cb9dae",
      "UserBalance": "112"
    },
    {
      "UserId": "975",
      "AccessToken": "251a12d4-2e9a-435e-bd80-537a82df2dc3",
      "UserBalance": "37"
    },
    {
      "UserId": "979",
      "AccessToken": "f3f672b6-6469-4006-935a-9a40a766738c",
      "UserBalance": "17"
    },
    {
      "UserId": "989",
      "AccessToken": "b062306c-2917-48bc-98b6-20fd66865c33",
      "UserBalance": "33"
    },
    {
      "UserId": "983",
      "AccessToken": "85590b78-d899-4275-aaa6-5db54f3fed86",
      "UserBalance": "11"
    },
    {
      "UserId": "985",
      "AccessToken": "71b1ce2d-0986-48bf-a5c1-2626b77b5476",
      "UserBalance": "181"
    },
    {
      "UserId": "993",
      "AccessToken": "52b55182-4749-4c99-ae16-e257656ffddb",
      "UserBalance": "2874"
    },
    {
      "UserId": "995",
      "AccessToken": "ac94ab2a-5fda-4d15-960e-103f3587876a",
      "UserBalance": "122"
    },
    {
      "UserId": "999",
      "AccessToken": "4db812b0-4345-432e-820a-9b3cc7596d01",
      "UserBalance": "261"
    },
    {
      "UserId": "1007",
      "AccessToken": "57f7e31a-cd1b-45fb-98fe-3faf17586bcc",
      "UserBalance": "2056"
    },
    {
      "UserId": "1009",
      "AccessToken": "c83ac4c0-d0f3-4459-83b2-b6844b98657d",
      "UserBalance": "29"
    },
    {
      "UserId": "1011",
      "AccessToken": "4420bc31-466a-4b1d-b380-b976fef65344",
      "UserBalance": "61810"
    },
    {
      "UserId": "1013",
      "AccessToken": "b59a4c42-d9bb-448e-9b69-3f19e766d4fe",
      "UserBalance": "253"
    },
    {
      "UserId": "1015",
      "AccessToken": "b0119e4b-5a98-4bd5-9d6a-4297df77548b",
      "UserBalance": "68"
    },
    {
      "UserId": "1017",
      "AccessToken": "4df4544b-1171-4796-8177-6e9760db4018",
      "UserBalance": "35"
    },
    {
      "UserId": "1019",
      "AccessToken": "c01ea631-9799-4771-8fb0-2fc41d456021",
      "UserBalance": "2186"
    },
    {
      "UserId": "1021",
      "AccessToken": "8345e0b0-4351-4b51-8ac6-ef213587f05e",
      "UserBalance": "4338"
    },
    {
      "UserId": "1023",
      "AccessToken": "96a29319-fd18-4a19-bf4a-51bd74656730",
      "UserBalance": "973"
    },
    {
      "UserId": "1025",
      "AccessToken": "c05a565b-a8df-41cc-a115-5618eea005da",
      "UserBalance": "209"
    },
    {
      "UserId": "1027",
      "AccessToken": "8a6639c0-69aa-431b-9bd8-432330340cea",
      "UserBalance": "68993"
    },
    {
      "UserId": "1029",
      "AccessToken": "fa975d24-d556-4930-9376-c0b2aaf5da90",
      "UserBalance": "2037"
    },
    {
      "UserId": "1031",
      "AccessToken": "fa2dc1ea-c162-49c2-81e1-223b734f60a8",
      "UserBalance": "8"
    },
    {
      "UserId": "1033",
      "AccessToken": "77ead4f8-3589-40fc-b461-8995a1f65c15",
      "UserBalance": "75"
    },
    {
      "UserId": "1035",
      "AccessToken": "b30a4cec-85f3-403f-b01c-b6be9943d2eb",
      "UserBalance": "224"
    },
    {
      "UserId": "1037",
      "AccessToken": "34f5836f-a53c-4c85-ac55-7c2b010529c9",
      "UserBalance": "6"
    },
    {
      "UserId": "1043",
      "AccessToken": "cf7651b4-a604-46e5-b198-fac3ce937dc7",
      "UserBalance": "1"
    },
    {
      "UserId": "1045",
      "AccessToken": "7eaa248d-8ad1-49eb-a3d8-d7e00ef5afe0",
      "UserBalance": "746"
    },
    {
      "UserId": "1047",
      "AccessToken": "49911c97-4704-4f3d-bf29-1aade20b160b",
      "UserBalance": "113"
    },
    {
      "UserId": "1049",
      "AccessToken": "a1620439-93ba-4f31-aae9-174967f96903",
      "UserBalance": "3846"
    },
    {
      "UserId": "1051",
      "AccessToken": "316c2311-c032-4877-b4a0-1154f83c8789",
      "UserBalance": "75"
    },
    {
      "UserId": "1053",
      "AccessToken": "87c27700-2d73-4189-badc-32b54589f642",
      "UserBalance": "156"
    },
    {
      "UserId": "1055",
      "AccessToken": "accea975-d33c-4e3b-9d05-f2c0479c7337",
      "UserBalance": "0"
    },
    {
      "UserId": "1057",
      "AccessToken": "bc16f018-0dc2-4663-8410-8205db8527e5",
      "UserBalance": "796"
    },
    {
      "UserId": "1059",
      "AccessToken": "e49a40a7-0026-4cab-9680-00f94090545d",
      "UserBalance": "0"
    },
    {
      "UserId": "1061",
      "AccessToken": "8c6f3a0d-1a58-4e78-9a8d-f8e346d7e489",
      "UserBalance": "27"
    },
    {
      "UserId": "1063",
      "AccessToken": "ce24c510-846f-4c9f-981c-3f8cc4173c5c",
      "UserBalance": "4326"
    },
    {
      "UserId": "1067",
      "AccessToken": "43844a47-d656-4acd-b443-96c739aee878",
      "UserBalance": "20035"
    },
    {
      "UserId": "1069",
      "AccessToken": "38aabcc0-ca53-42fb-a0ef-e7296b56a4a5",
      "UserBalance": "3"
    },
    {
      "UserId": "1071",
      "AccessToken": "e05d1f39-b5f0-410d-9b75-b5fbae2342b1",
      "UserBalance": "263"
    },
    {
      "UserId": "1075",
      "AccessToken": "16bde405-f231-4731-82fd-ba8cc8b72f3e",
      "UserBalance": "60"
    },
    {
      "UserId": "1077",
      "AccessToken": "14ef2363-dbf7-4ee1-92a6-653bab648da2",
      "UserBalance": "482"
    },
    {
      "UserId": "1081",
      "AccessToken": "f8d3e77d-5509-4ad6-9dfd-db6c177eb058",
      "UserBalance": "72"
    },
    {
      "UserId": "1083",
      "AccessToken": "af3afc3c-af82-43ed-8b24-29695b74cf8b",
      "UserBalance": "1"
    },
    {
      "UserId": "1087",
      "AccessToken": "498f747a-a910-456f-8dfb-d47fdc3d6481",
      "UserBalance": "24"
    },
    {
      "UserId": "1089",
      "AccessToken": "70c7874e-28b0-4346-9d4c-4d4c80cfd0eb",
      "UserBalance": "2"
    }
  ]
}
            """
        val userInfo = ObjectMapper().readValue(ss2, HunterUserInfoResp::class.java)
        log.info(userInfo.toString())
    }
}

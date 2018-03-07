<!DOCTYPE html>
<html>
<head>
    <title>规则引擎</title>
    <meta charset="utf-8">
    <style type="text/css">
        table, td, th
        {
            border:1px solid green;
        }

        th
        {
            background-color:green;
            color:white;
        }
    </style>
</head>
<body>
<div>
    <form>
        <div>
            <table style="width: 100%;">
                <thead>
                    <th>促销名称</th>
                    <th>标签</th>
                    <th>维度</th>
                    <th>判断</th>
                    <th>条件</th>
                    <th>则</th>
                    <th>促销行为</th>
                    <th>促销结果</th>
                </thead>
                <tbody>
                    <tr style="display: none">
                        <td><input type="text" name="ruleName"></td>
                        <td>
                            <select name="mylabel">
                                <option value="">--请选择--</option>
                                <option value="爆款">爆款</option>
                                <option value="双十一">双十一</option>
                                <option value="红包节">红包节</option>
                                <option value="年货节">年货节</option>
                            </select>
                        </td>
                        <td>
                            <select name="my_dimension">
                                <option value="">--请选择--</option>
                                <option value="总金额">金额</option>
                                <option value="总件数">件数</option>
                            </select>
                        </td>
                        <td>
                            <select name="my_judge">
                                <option value="">--请选择--</option>
                                <option value="大于">大于</option>
                                <option value="小于">小于</option>
                                <option value="在区间">在区间</option>
                            </select>
                        </td>
                        <td>
                            <input type="text" name="my_condition" value="" placeholder="门槛件数或金额">
                        </td>
                        <td>则</td>
                        <td>
                            <select name="promotion_behave">
                                <option value="">--请选择--</option>
                                <option value="单价减">单价减</option>
                                <option value="总金额折扣">总金额折扣</option>
                                <option value="总金额满减">总金额满减</option>
                                <option value="送赠品">送赠品</option>
                                <option value="送优惠券">送优惠券</option>
                            </select>
                        </td>
                        <td>
                            <input type="text" name="promotion_result">
                        </td>
                    </tr>
                </tbody>
            </table>
            <div class="productList">
                <div class="product" style="display: none">
                    商品名称：<input type="text" name="productName" value="">
                    商品标签：<select name="productLabel">
                                <option value="">--请选择--</option>
                                <option value="爆款">爆款</option>
                                <option value="双十一">双十一</option>
                                <option value="红包节">红包节</option>
                                <option value="年货节">年货节</option>
                            </select>
                    商品数量：<input type="text" name="productNum" value="">
                    商品价格：<input type="text" name="productPrice" value="">
                </div>
            </div>

            <div style="text-align: center;">
                <input type="button" id="jisuan" value="提交计算">
                <input type="button" id="addRule" value="添加规则">
                <input type="button" id="addProduct" value="添加商品">
            </div>
            <div><span>计算结果：</span><textarea style="margin: 5px;width: 994px;height: 331px;"></textarea></div>
        </div>
    </form>
</div>

<script type="text/javascript" src="/${baseContext}/js/jquery/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="/${baseContext}/js/ruleEngine/rule-engine.js"></script>
<script type="text/javascript">
</script>
</body>
</html>
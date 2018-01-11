<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="Cache-Control" content="no-cache,must-revalidate" />
    <meta http-equiv="expires" content="Wed,26 Feb 1997 08:21:57 GMT" />
    <meta name="format-detection" content="telephone=no" />
    <title>Document</title>
    <link rel="stylesheet" href="common.css"></link>
    <link rel="stylesheet" href="author2.css"></link>
</head>
<body>
<div class="page main-page" data-role="page">
    <div data-role="header" id="page-header">
        <div id="author-title">服务授权</div>
        <div id="third-icon">
            <img src>
        </div>
        <div id="info">
					<span>
                    ${title}
					</span>
        </div>
    </div>
    <div data-role="content">
        <form id='confirmationForm' name='confirmationForm' action='authorize' method='post'>
            <input name='user_oauth_approval' value='true' type='hidden'/>
            你同意授权吗
            <#--<div id="check-list">-->
                <#--<#list scopes as node>-->
                    <#--<ul>-->
                        <#--<li>-->
                            <#--<input type="checkbox" name="scope.${node}" value="true" checked="checked" disabled="disabled">-->
                            <#--<input name='scope.${node}' value='true' type='hidden'/>-->
                            <#--<#assign desc=node + "_desc" d=desc?eval>-->
                            <#--<span>${d}</span>-->
                        <#--</li>-->
                    <#--</ul>-->
                <#--</#list>-->
            <#--</div>-->
            <input id="confirmBtn" type='submit' data-role="none" value="确定"/>
        </form>
    </div>
</div>
<script type="text/javascript" src="jquery.js"></script>
<script type="text/javascript" src="jquery.mobile-1.4.5.min.js"></script>
</body>
</html>
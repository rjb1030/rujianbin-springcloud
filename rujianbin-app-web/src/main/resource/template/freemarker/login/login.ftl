<!DOCTYPE html>
<!--[if lt IE 7]> <html class="lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]> <html class="lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]> <html class="lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html lang="en"> <!--<![endif]-->
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>Login Page</title>
    <link rel="stylesheet" href="css/login/login.css">
    <script type="text/javascript" src="js/jquery/jquery-1.7.2.min.js"></script>
    <script type="text/javascript">
        $(function(){
            $('#kaptcha').click(function(){
                        var _t='common/kaptcha?'+new Date().getTime();
                        $(this).attr("src",_t);
            });
        })
    </script>
</head>
<body>
<form action="login-submit" class="login" method="post">
    <h1>login page</h1>
    <#--<input type="hidden" -->
           <#--name="${_csrf.parameterName}"-->
           <#--value="${_csrf.token}"/>-->
    <input type="text" name="username" class="login-input" placeholder="Username" autofocus>
    <input type="password" name="password" class="login-input" placeholder="Password">
    <div>
        <input type="text" name="vCode" class="login-input" style="display: inline;width: 90px;" placeholder="验证码">
        <img src="common/kaptcha" id="kaptcha" style="width:100px;float:right;">
    </div>
    <input type="submit" value="Login" class="login-submit">
    <p class="login-help"><a target="_blank" href="http://www.baidu.com">Forgot password?</a></p>
</form>

<section class="about">
    <p class="about-links">
        <a href="http://www.cssflow.com/snippets/facebook-login-form" target="_parent">View Article</a>
        <a href="http://www.cssflow.com/snippets/facebook-login-form.zip" target="_parent">Download</a>
    </p>
    <p class="about-author">
        &copy; 2013 <a href="http://thibaut.me" target="_blank">Thibaut Courouble</a> -
        <a href="http://www.cssflow.com/mit-license" target="_blank">MIT License</a><br>
        Original PSD by <a href="http://dribbble.com/shots/808325-Facebook-Login-Freebie" target="_blank">Alex Montague</a>
    </p>
</section>
</body>
</html>
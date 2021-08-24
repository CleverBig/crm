<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<meta charset="UTF-8">

	<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

	<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>

	<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

	<script type="text/javascript">

	$(function(){
		$("#addBtn").click(function(){
			// 发送ajax查询所有用户
			$.ajax({
				url: "workbench/activity/getUser.do",
				type: "get",
				dataType: "json",
				success: function(data){
					$.each(data,function(i,v){
						$("#create-owner").append("<option value="+v.id+">"+v.name+"</option>");
					})
				}
			})

			// 打开创建市场活动的模态框
			$("#createActivityModal").modal("show");

			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});
		})

		$("#saveBtn").click(function(){
			// 提交异步请求保存市场活动
			$.ajax({
				url: "workbench/activity/save.do",
				data: {
					"owner": $.trim($("#create-owner").val()),
					"name": $.trim($("#create-name").val()),
					"startDate": $.trim($("#create-startDate").val()),
					"endDate": $.trim($("#create-endDate").val()),
					"cost": $.trim($("#create-cost").val()),
					"description": $.trim($("#create-description").val())
				},
				type: "post",
				dataType: "json",
				success: function(data){
					if(data){
						$("#activityAddForm")[0].reset();
						// 打开创建市场活动的模态框
						$("#createActivityModal").modal("hide");
						// 提交新增修改删除后查询市场活动列表中的数据
						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
						alert("保存成功");
					}else{
						alert("保存失败");
					}
				}
			})
		})

		// 当市场活动页面加载完毕后查询市场活动列表中的数据
		pageList(1,5);

		// 点击查询按钮后查询市场活动列表中的数据
		$("#searchBtn").click(function(){
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startDate").val()));
			$("#hidden-endDate").val($.trim($("#search-endDate").val()));
			pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
		})

		// 全选/全不选
		$("#selectAll").click(function(){
			$("input[name='subSelect']").prop("checked",this.checked);
		})

		// 子选父改变状态
		$("#activityBody").on("click",function(){
			// 父选框是否自动勾选，取决于总的子选框数量是否等于被勾选的子选框数量，返回true则自动勾选，返回false则不勾选
			$("#selectAll").prop("checked",$("input[name='subSelect']").length==$("input[name='subSelect']:checked").length);
		})

		$("#deleteBtn").click(function(){
			// 获取所有被勾选的记录行
			var subSelects = $("input[name=subSelect]:checked");
			if(subSelects.length==0){
				alert("请选择要删除的记录行");
			}else{
				// 定义字符串完成id拼接
				var ids = "";
				for (var i = 0; i < subSelects.length; i++) {
					var id = $(subSelects[i]).prop("id");
						ids += "id="+id;
					if(i < subSelects.length-1){
						ids += "&";
					}
				}

				$.ajax({
					url: "workbench/activity/delete.do",
					data: ids,
					type: "post",
					dataType: "json",
					success: function(data){
						if(data){
							// 刷新市场活动列表
							pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
							alert("删除成功");
						}else{
							alert("删除失败");
						}
					}
				})
			}

		})

		$("#editBtn").click(function(){
			var $select = $("input[name=subSelect]:checked");
			if($select.length == 0){
				alert("请选择要修改的记录行");
			}else if($select.length > 1){
				alert("只能选择一条要修改的记录行");
			}else{
				var id = $select.val();
				// 查询用户名和市场活动对象
				$.ajax({
					url: "workbench/activity/getUserAndActivityList.do",
					data: {
						id
					},
					type: "get",
					dataType: "json",
					success: function(data){
						$("#edit-owner").html("");
						// 接收用户名和市场活动对象
						$("#edit-id").val(data.activity.id);
						$.each(data.userList,function(i,v){
							$("#edit-owner").append("<option value="+v.id+">"+v.name+"</option>");
						})
						$("#edit-name").val(data.activity.name);
						$("#edit-startDate").val(data.activity.startDate);
						$("#edit-endDate").val(data.activity.endDate);
						$("#edit-cost").val(data.activity.cost);
						$("#edit-description").val(data.activity.description);
					}
				})
				// 显示修改操作的模态窗口
				$("#editActivityModal").modal("show");

			}


		})

		$("#updateBtn").click(function(){
			// 提交异步请求保存市场活动
			$.ajax({
				url: "workbench/activity/update.do",
				data: {
					"id": $.trim($("#edit-id").val()),
					"owner": $.trim($("#edit-owner").val()),
					"name": $.trim($("#edit-name").val()),
					"startDate": $.trim($("#edit-startDate").val()),
					"endDate": $.trim($("#edit-endDate").val()),
					"cost": $.trim($("#edit-cost").val()),
					"description": $.trim($("#edit-description").val())
				},
				type: "post",
				dataType: "json",
				success: function(data){
					if(data.success){
						// 关闭修改市场活动的模态框
						$("#editActivityModal").modal("hide");
						// 提交修改后查询市场活动列表中的数据
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
						alert("修改成功");
					}else{
						alert("修改失败");
					}
				}
			})
		})

	})

	// 查询所有市场活动列表中的数据
	function pageList(pageNo,pageSize){
		// 取消父选框的勾选状态
		$("#selectAll").prop("checked",false);

		// 每次查询市场活动列表中的数据前先从隐藏域中取出数据
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-startDate").val($.trim($("#hidden-startDate").val()));
		$("#search-endDate").val($.trim($("#hidden-endDate").val()));

		$.ajax({
			url: "workbench/activity/pageList.do",
			data: {
				"name": $.trim($("#search-name").val()),
				"owner": $.trim($("#search-owner").val()),
				"startDate": $.trim($("#search-startDate").val()),
				"endDate": $.trim($("#search-endDate").val()),
				"pageNo": pageNo,
				"pageSize": pageSize
			},
			type: "get",
			dataType: "json",
			success: function(data){
				$("#activityBody").html("");
				// 要获取到的结果数据: 查询到的所有结果以及查询到的结果总条数
				$.each(data.dataList,function(i,v){
					$("#activityBody").append(
							'<tr class="active">'+
								'<td><input name="subSelect" type="checkbox" value="'+v.id+'"/></td>'+
								'<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+v.id+'\';" value="">'+v.name+'</a></td>'+
								'<td>'+v.owner+'</td>'+
								'<td>'+v.startDate+'</td>'+
								'<td>'+v.endDate+'</td>'+
							'</tr>'
					);
				})
				// 计算总页数
				var totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;

				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});
			}
		})
	}

	</script>
</head>
<body>
	<!-- 存储查询条件的隐藏域 -->
	<input id="hidden-name" type="hidden"/>
	<input id="hidden-owner" type="hidden"/>
	<input id="hidden-startDate" type="hidden"/>
	<input id="hidden-endDate" type="hidden"/>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="activityAddForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">

								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate" style="background-color: white" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate" style="background-color: white" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startDate">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>

	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input id="search-name" class="form-control" type="text">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input id="search-owner" class="form-control" type="text">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input id="search-startDate" class="form-control time" type="text"/>
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input id="search-endDate" class="form-control time" type="text"/>
				    </div>
				  </div>
				  
				  <button id="searchBtn" type="button" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBtn" ><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input id="selectAll" type="checkbox" /></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">

					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage"></div>
			</div>
			
		</div>
		
	</div>
</body>
</html>
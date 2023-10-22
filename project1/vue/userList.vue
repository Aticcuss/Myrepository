<template>
    <div>
        <el-container>
            <el-header>
                <el-row :gutter="5">
                    <el-col :span="4">
                        <div> <el-input style="width: 180px" size="small" placeholder="请输入ID" v-model="idIndex"
                                clearable></el-input></div>
                    </el-col>
                    <el-col :span="4">
                        <div> <el-input style="width: 180px" size="small" placeholder="请输入用户名" v-model="userNameIndex"
                                clearable></el-input></div>
                    </el-col>
                    <el-col :span="4">
                        <div> <el-input style="width: 180px" size="small" placeholder="请输入昵称" v-model="nickNameIndex"
                                clearable></el-input></div>
                    </el-col>
                    <el-col :span="1">
                        <div><el-button type="primary" size="small" @click="find">搜索</el-button></div>
                    </el-col>
                    <el-col :span="1">
                        <div><el-button class="button" type="warning" size="small" @click="reset">重置</el-button></div>
                    </el-col>

                </el-row>
                <el-row :gutter="10">


                </el-row>

            </el-header>
            <el-main>
                <el-table :data="tableData" style="width: 100%">
                    <el-table-column prop="userId" label="用户ID">
                    </el-table-column>
                    <el-table-column prop="userName" label="用户名">
                    </el-table-column>
                    <el-table-column prop="nickName" label="昵称">
                    </el-table-column>
                    <el-table-column prop="phone" label="电话号码">
                    </el-table-column>
                    <el-table-column label="权限">
                        <template slot-scope="scope">
                            <el-tag v-if="scope.row.privileges == 0">普通用户</el-tag>
                            <el-tag type="danger" v-if="scope.row.privileges == 1">管理员</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column label="状态" :filter-method="filterState" :filters="[{text:'启用',value:0},{text:'封禁',value:1}]">
                        <template slot-scope="scope">
                            <el-tag type="success" effect="dark" v-if="scope.row.state == 0">启用</el-tag>
                            <el-tag type="danger" effect="dark" v-if="scope.row.state == 1">封禁</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作">
                        <template slot-scope="scope">
                            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
                            <el-popconfirm confirm-button-text='确定' cancel-button-text='取消' icon="el-icon-info"
                                icon-color="red" title="确定要删除吗？" @confirm="deleteSubmit(scope.row.userId)">
                                <el-button class="button" size="mini" type="danger" slot="reference">删除</el-button>
                            </el-popconfirm>
                        </template>
                    </el-table-column>
                </el-table>
            </el-main>
        </el-container>

        <el-dialog title="用户信息更改" :visible.sync="dialogFormVisible" style="text-align: center;">
            <el-form :model="form" :rules="editRules" ref="updateForm" label-positon="left" label-width="auto">
                <el-form-item label="昵称" prop="nickName">
                    <el-input v-model="form.nickName"></el-input>
                </el-form-item>
                <el-form-item label="电话" prop="phone">
                    <el-input v-model="form.phone"></el-input>
                </el-form-item>
                <el-form-item label="用户权限">
                    <el-radio v-model="form.privileges" :label="0">普通用户</el-radio>
                    <el-radio v-model="form.privileges" :label="1">管理员</el-radio>
                </el-form-item>
                <el-form-item label="状态">
                    <el-radio v-model="form.state" :label="0">启用</el-radio>
                    <el-radio v-model="form.state" :label="1">封禁</el-radio>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="editSubmit('updateForm')">提 交</el-button>
            </div>
        </el-dialog>

        <div class="block">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pageNum"
                :page-sizes="[5, 10, 20]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
                :total="total">
            </el-pagination>
        </div>
    </div>
</template>
  
<script>

import request from '../utils/request.js';

export default {

    created() {
        this.loadList();
    },
    data() {
        var checkPhone = (rule, value, callback) => {
            //13位手机号
            let reg = /^[1][3,4,5,6,7,8,9][0-9]{9}$/;
            if (!reg.test(value)) {
                callback(new Error('请输入11位手机号'));
            } else {
                callback();
            }
        };
        var checkBlank = (rule, value, callback) => {
            //检查受否存在空格
            let reg = /^\S*$/;
            if (!reg.test(value)) {
                callback(new Error('字符串中不能包含空格!'));
            } else {
                callback();
            }
        };
        return {
            //当前记录数
            total: 0,
            //列表数据
            tableData: [],
            //搜索框数据
            //弹出框是否可见
            dialogFormVisible: false,
            //表单数据
            form: {
                userId: 0,
                userName: '',
                nickName: '',
                phone: '',
                privileges: 0,
                state: 0,
            },
            //分页数据
            pageNum: 1,
            pageSize: 5,
            //用于搜索的索引
            idIndex: "",
            userNameIndex: "",
            nickNameIndex: "",
            //表单验证规则
            editRules: {
                nickName: [
                    { required: true, message: '请输入昵称', trigger: 'blur' },
                    { min: 3, max: 8, message: '长度在3到8个字符', trigger: 'blur' },
                    { validator: checkBlank, trigger: 'blur' }
                ],
                phone: [
                    { required: true, message: '请输入11位电话号码', trigger: 'blur' },
                    { validator: checkPhone, trigger: 'blur' }
                ]

            }
        }
    },
    methods: {
        filterState(row,value){
            console.log()
        },
        handleEdit(row) {
            //表单填充原始数据
            this.form.userId = row.userId;
            this.form.nickName = row.nickName;
            this.form.phone = row.phone;
            this.form.privileges = row.privileges;
            this.form.state = row.state;
            this.dialogFormVisible = true;
        },
        //表单验证
        editSubmit(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    request.post("http://localhost:8080/user/save", this.form).then(res => {
                        console.log(this.form);
                        if (res == 1) {
                            this.$message.success("操作成功");
                            this.loadList();
                            this.dialogFormVisible = false;
                        } else {
                            this.$message.error("操作失败");
                        }

                    }).catch(error=>{
                        console.log("update axios error!")
                        console.log(error);
                    })
                } else {
                    console.log('error submit!!');
                    return false;
                }
            })
        },
        //删除按钮触发函数

        deleteSubmit(userId) {

            request.delete("http://localhost:8080/user/del/" + userId).then(res => {
                if (res == 1) {
                    this.$message.success("删除成功");
                    //当前页数据被全部删除后，将切换到上一页
                    if ((this.total - 1) % this.pageSize == 0 && this.total != 0) {
                        this.pageNum = this.pageNum - 1;
                    }
                    this.loadList();
                    this.dialogFormVisible = false;
                } else {
                    this.$message.error("删除失败");
                }

            }).catch(error=>{
                console.log("delete axios error!")
                console.log(error);
            })
        },
        handleSizeChange(val) {
            this.pageSize = val;
            this.loadList();
        },
        handleCurrentChange(val) {
            this.pageNum = val;
            this.loadList();
        },
        //重置搜索
        reset() {
            this.idIndex = "";
            this.userNameIndex = "";
            this.nickNameIndex = "";
            this.loadList();
        },
        //搜索
        find() {

            this.pageNum = 1;
            this.loadList();
        },
        loadList() {
            request.get("http://localhost:8080/user/page",
                {
                    params: {
                        userId: this.idIndex,
                        pageNum: this.pageNum,
                        pageSize: this.pageSize,
                        userName: this.userNameIndex,
                        nickName: this.nickNameIndex
                    }
                }
            ).then(res => {
                console.log(res);
                this.tableData = res.data;
                this.total = res.total;
            }).catch(error=>{
                console.log("loadList axios error!")
                console.log(error);
            })
        }
    }
}
</script>
<style>
.el-row {
    margin-bottom: 10px;
}

.button {
    margin-left: 5px;
}
</style>
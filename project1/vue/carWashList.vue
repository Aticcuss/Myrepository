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
                        <div> <el-input style="width: 180px" size="small" placeholder="请输入车店名" v-model="cw_nameIndex"
                                clearable></el-input></div>
                    </el-col>
                    <el-col :span="4">
                        <div> <el-input style="width: 180px" size="small" placeholder="请输入车店地址" v-model="locationIndex"
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
                    <el-col :span="4">
                        <div><el-button type="primary" size="small" @click="insert">添加</el-button></div>
                    </el-col>

                </el-row>

            </el-header>
            <el-main>
                <el-table :data="tableData" style="width: 100%">
                    <el-table-column prop="cw_id" label="车店ID">
                    </el-table-column>
                    <el-table-column prop="cw_name" label="店名">
                    </el-table-column>
                    <el-table-column prop="location" label="位置">
                    </el-table-column>
                    <el-table-column prop="openhour" label="营业时间">
                    </el-table-column>
                    <el-table-column prop="score" label="评分">
                        <template slot-scope="scope">
                            <el-rate v-model="scope.row.score" disabled show-score text-color="#ff9900"
                                v-if="scope.row.score > 0" score-template="{value}">
                            </el-rate>
                            <p v-else>暂无评分</p>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作">
                        <template slot-scope="scope">
                            <el-button size="mini" @click="handleEdit(scope.$index)">详情</el-button>
                            <el-popconfirm confirm-button-text='确定' cancel-button-text='取消' icon="el-icon-info"
                                icon-color="red" title="确定要删除吗？" @confirm="deleteSubmit(scope.row.cw_id)">
                                <el-button class="button" size="mini" type="danger" slot="reference">删除</el-button>
                            </el-popconfirm>
                        </template>
                    </el-table-column>
                </el-table>
            </el-main>
        </el-container>

        <el-dialog title="车店详细信息" :visible.sync="dialogFormVisible" style="text-align: left;">
            <el-form :model="form" :rules="editRules" ref="updateForm" label-positon="left" label-width="auto">
                <el-form-item label="车店名称" prop="cw_name">
                    <el-input v-model="form.cw_name"></el-input>
                </el-form-item>
                <el-form-item label="位置" prop="location">
                    <el-input v-model="form.location"></el-input>
                </el-form-item>
                <el-form-item label="店主" prop="owner">
                    <el-input :disabled="true" v-model="ownerName"></el-input>
                </el-form-item>
                <el-form-item label="营业时间" prop="openhour">
                    <el-time-select placeholder="起始时间" v-model="startTime" :picker-options="{
                        start: '08:30',
                        step: '00:15',
                        end: '18:30'
                    }">
                    </el-time-select>
                    <el-time-select style="margin-left: 5px" placeholder="结束时间" v-model="endTime" :picker-options="{
                        start: '08:30',
                        step: '00:15',
                        end: '18:30',
                        minTime: startTime
                    }">
                    </el-time-select>
                </el-form-item>
                <el-form-item label="联系方式" prop="cw_phone">
                    <el-input v-model="form.cw_phone"></el-input>
                </el-form-item>
                <el-form-item label="评分" prop="score" v-if="this.form.cw_id > 0 && this.form.score>0">
                    <el-rate v-model="this.form.score" disabled show-score text-color="#ff9900" score-template="{value}">
                    </el-rate>
                </el-form-item>
                <el-form-item label="描述" prop="description">
                    <el-input type="textarea" v-model="form.description"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="editSubmit('updateForm')">提 交</el-button>
            </div>
        </el-dialog>

        <div class="block">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pageNum"
                :page-sizes="[2, 5, 10, 20]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
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
            //检查是否存在空格
            let reg = /^\S*$/;
            if (!reg.test(value)) {
                callback(new Error('字符串中不能包含空格!'));
            } else {
                callback();
            }
        };
        //检查营业时间
        var checkTime = (rule, value, callback) => {

            if (this.startTime >= this.endTime) {
                callback(new Error('开始时间必须大于结束时间'));
            } else {
                callback();
            }
        };
        return {
            //
            userId: 2,
            ownerName: '',
            startTime: '08:30',
            endTime: '18:30',
            //当前记录数
            total: 0,
            //列表数据
            tableData: [],
            //搜索框数据
            //弹出框是否可见
            dialogFormVisible: false,
            //表单数据
            form: {
                cw_id: 0,
                cw_name: '',
                location: '',
                cw_phone: '',
                description: '',
                cw_image: '',
                openhour: '',
                owner: 0,
                score: 0,
            },
            //分页数据
            pageNum: 1,
            pageSize: 2,
            //用于搜索的索引
            idIndex: "",
            cw_nameIndex: "",
            locationIndex: "",
            //表单验证规则
            editRules: {
                cw_phone: [
                    { required: true, message: '请输入11位电话号码', trigger: 'blur' },
                    { validator: checkPhone, trigger: 'blur' }
                ],
                cw_name: [
                    { required: true, message: '请输入车店名称', trigger: 'blur' },
                    { min: 3, max: 150, message: "长度必须在3-50个字符", trigger: 'blur' },
                    { validator: checkBlank, trigger: 'blur' }
                ],
                location: [
                    { required: true, message: '请输入车店地址', trigger: 'blur' },
                    { min: 3, max: 150, message: "长度必须在3-150个字符", trigger: 'blur' },
                    { validator: checkBlank, trigger: 'blur' }
                ],
                openhour: [
                    { validator: checkTime, trigger: 'blur' }
                ],
                description: [
                    { max: 190, message: "长度必须在190个字符以内", trigger: 'blur' }
                ]
            }
        }
    },
    methods: {
        insert() {
            request.get("http://localhost:8080/user/find",
                {
                    params: {
                        userId: this.userId,
                    }
                }
            ).then(res => {
                this.ownerName = res[0].userName;
            }).catch(error => {
                console.log("loadList axios error!")
                console.log(error);
            })
            this.form.cw_id = -1;
            this.dialogFormVisible = true;
            this.form.cw_image = "image"
            this.form.cw_name = "";
            this.form.location = "";
            this.form.cw_phone = "";
            this.form.description = "";
            this.form.owner = this.userId;
            this.dialogFormVisible = true;
        },
        handleEdit(index) {
            //表单填充原始数据
            //根据店主ID查找店主用户名
            request.get("http://localhost:8080/user/find",
                {
                    params: {
                        userId: this.tableData[index].owner,
                    }
                }
            ).then(res => {
                this.ownerName = res[0].userName;
            }).catch(error => {
                console.log("loadList axios error!")
                console.log(error);
            })
            //解析openhour
            let temp = this.tableData[index].openhour.split('-');
            this.startTime = temp[0];
            this.endTime = temp[1];

            this.form.openhour = this.tableData[index].openhour;
            this.form.cw_id = this.tableData[index].cw_id;
            this.form.cw_name = this.tableData[index].cw_name;
            this.form.location = this.tableData[index].location;
            this.form.cw_phone = this.tableData[index].cw_phone;
            this.form.description = this.tableData[index].description;
            this.form.cw_image = this.tableData[index].cw_image;
            this.form.score = this.tableData[index].score;
            this.form.owner = this.tableData[index].owner;
            this.dialogFormVisible = true;
        },
        //表单验证
        editSubmit(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    //拼接openhour
                    this.form.openhour = this.startTime + '-' + this.endTime;
                    console.log(this.form);
                    request.post("http://localhost:8080/carWash/save", this.form).then(res => {
                        console.log(this.form);
                        if (res == 1) {
                            this.$message.success("操作成功");
                            this.loadList();
                            this.dialogFormVisible = false;
                        } else {
                            this.$message.error("操作失败,请检查店名或者位置是否已经存在");
                        }

                    }).catch(error => {
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

        deleteSubmit(cw_id) {

            request.delete("http://localhost:8080/carWash/del/" + cw_id).then(res => {
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

            }).catch(error => {
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
            this.cw_nameIndex = "";
            this.locationIndex = "";
            this.idIndex = "";
            this.loadList();
        },
        //搜索
        find() {
            this.pageNum = 1;
            this.loadList();
        },
        loadList() {
            request.get("http://localhost:8080/carWash/page",
                {
                    params: {
                        pageNum: this.pageNum,
                        pageSize: this.pageSize,
                        cw_name: this.cw_nameIndex,
                        location: this.locationIndex,
                        cw_id: this.idIndex
                    }
                }
            ).then(res => {
                console.log(res);
                this.tableData = res.data;
                this.total = res.total;
            }).catch(error => {
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
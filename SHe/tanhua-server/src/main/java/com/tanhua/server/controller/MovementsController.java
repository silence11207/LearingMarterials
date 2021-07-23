package com.tanhua.server.controller;

import com.tanhua.dubbo.server.pojo.Movements;
import com.tanhua.server.service.MovementsService;
import com.tanhua.server.service.QuanziMQService;
import com.tanhua.server.vo.PageResult;
import com.tanhua.server.vo.VisitorsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("movements")
@RestController
public class MovementsController {
    @Autowired
    private MovementsService movementsService;
    @Autowired
    private QuanziMQService quanziMQService;

    /**
     *  发布动态
     * @param textContent
     * @param location
     * @param longitude
     * @param latitude
     * @param multipartFiles
     * @param token
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> saveMovements(@RequestParam("textContent")String textContent,
                                              @RequestParam("location")String location,
                                              @RequestParam("longitude")String longitude,
                                              @RequestParam("latitude")String latitude,
                                              @RequestParam("imageContent")MultipartFile[] multipartFiles,
                                              @RequestHeader("Authorization")String token){

        try {
            String publishId = this.movementsService.saveMovements(textContent, location, longitude, latitude, multipartFiles);
            if(StringUtils.isNotEmpty(publishId)){
                //发送消息
                this.quanziMQService.sendSavePublishMsg(publishId);
                return ResponseEntity.ok(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 查询好友动态
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping
    public PageResult queryPublishList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                       @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize) {
        return this.movementsService.queryPublishList(page, pageSize, false);
    }

    /**
     * 查询推荐动态
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("recommend")
    public PageResult queryRecommendPublishList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize) {
        return this.movementsService.queryPublishList(page, pageSize, true);
    }

    /**
     * 点赞的操作
     * @param publishId
     * @return
     */
    @GetMapping("/{id}/like")
    public ResponseEntity<Long> likeComment(@PathVariable("id") String publishId){
        try {
            Long count = this.movementsService.likeComment(publishId);
            if(null != count){
                //发送消息
                this.quanziMQService.likePublishMsg(publishId);
                return ResponseEntity.ok(count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }

    /**
     * 取消赞的操作
     * @param publishId
     * @return
     */
    @GetMapping("/{id}/dislike")
    public ResponseEntity<Long> dislikeComment(@PathVariable("id") String publishId){
        try {
            Long count = this.movementsService.cancelLikeComment(publishId);
            if(null != count){
                //发送消息
                this.quanziMQService.disLikePublishMsg(publishId);
                return ResponseEntity.ok(count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }

    /**
     * 喜欢的操作
     * @param publishId
     * @return
     */
    @GetMapping("/{id}/love")
    public ResponseEntity<Long> loveComment(@PathVariable("id") String publishId){
        try {
            Long loveCount = this.movementsService.loveComment(publishId);
            if (null != loveCount) {
                //发送消息
                this.quanziMQService.lovePublishMsg(publishId);
                return ResponseEntity.ok(loveCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }

    /**
     * 取消喜欢的操作
     * @param publishId
     * @return
     */
    @GetMapping("/{id}/unlove")
    public ResponseEntity<Long> unloveComment(@PathVariable("id") String publishId){
        try {
            Long loveCount = this.movementsService.cancelLoveComment(publishId);
            if (null != loveCount) {
                //发送消息
                this.quanziMQService.disLovePublishMsg(publishId);
                return ResponseEntity.ok(loveCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 查询单条动态信息
     * @param publishId
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<Movements> queryMovementsById(@PathVariable("id") String publishId){
        try {
            Movements movements = this.movementsService.queryMovementsById(publishId);
            if(null != movements){
                //发送消息
                this.quanziMQService.querySavePublishMsg(publishId);
                return ResponseEntity.ok(movements);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 谁看过我
     *
     * @return
     */
    @GetMapping("visitors")
    public ResponseEntity<List<VisitorsVo>> queryVisitorsList(){
        try {
            List<VisitorsVo> list = this.movementsService.queryVisitorsList();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("all")
    public ResponseEntity<PageResult> queryAlbumList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                     @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize,
                                                     @RequestParam(value = "userId") Long userId) {
        try {
            PageResult pageResult = this.movementsService.queryAlbumList(userId, page, pageSize);
            return ResponseEntity.ok(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }
}
